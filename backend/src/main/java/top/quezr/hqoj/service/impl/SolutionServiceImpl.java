package top.quezr.hqoj.service.impl;

import cn.hutool.core.util.StrUtil;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import top.quezr.hqoj.dao.esdao.EsSolutionDao;
import top.quezr.hqoj.entity.*;
import top.quezr.hqoj.enums.ItemType;
import top.quezr.hqoj.enums.LikeType;
import top.quezr.hqoj.mapper.SolutionMapper;
import top.quezr.hqoj.service.SolutionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.quezr.hqoj.support.PageInfo;
import top.quezr.hqoj.support.Result;
import top.quezr.hqoj.util.VeryCodeUtil;
import top.quezr.hqoj.util.event.AddSolutionEvent;
import top.quezr.hqoj.util.event.CenterEventBus;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author que
 * @since 2021-05-13
 */
@Service
@Slf4j
public class SolutionServiceImpl extends ServiceImpl<SolutionMapper, Solution> implements SolutionService {


    private static final String SOLUTION_LIKE_HASH_KEY = "hash:count:s:";
    private static final String SOLUTION_LIKE_LOCK_KEY = "lock:count:s:";

    @PostConstruct
    public void init(){
        CenterEventBus.bus.register(this);
    }

    @Autowired
    EsSolutionDao esSolutionDao;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public Result<PageInfo<Solution>> getSolutionPage(Integer problemId, Integer[] tags, String searchVal, PageInfo<Solution> pageInfo) {
        Result<PageInfo<Solution>> result = new Result<>();
        pageInfo = PageInfo.normalizing(pageInfo);

        String tagSearch = null;
        if (Objects.nonNull(tags) && tags.length>0){
            tagSearch = Arrays.toString(tags);
        }
        if (StrUtil.isBlank(searchVal)){
            List<Solution> solutions = baseMapper.getSolutionList(problemId,tagSearch,pageInfo.getPageSize(),pageInfo.getPageSize()*pageInfo.getPageNumber(),pageInfo.getLastId());
            pageInfo.setData(solutions);
            if (pageInfo.getHasCount()){
                Integer count = baseMapper.getSolutionListTotalCount(problemId,tagSearch);
                pageInfo.setTotalCount(count);
            }
            result.setData(pageInfo);
        } else {
            PageInfo<Solution> list = getSolutionListInEs(problemId, tags, searchVal, pageInfo);
            result.setData(list);
        }
        return result;
    }


    @Override
    public Result<Solution> getSolutionById(Integer id) {
        Result<Solution> result = new Result<>();
        Solution solution = baseMapper.selectById(id);
        if (Objects.isNull(solution)){
            result.setSuccess(false);
            result.setMessage("该题解不存在！");
            return result;
        }
        result.setData(solution);
        return result;
    }

    @Override
    public Result<Void> addSolution(Solution solution) {
        baseMapper.insert(solution);
        CenterEventBus.bus.post(new AddSolutionEvent(solution));
        return new Result<>();
    }

    /**
     * 通过es搜索题解
     * @param problemId NOT_NULL
     * @param tags NULL_ABLE
     * @param searchVal NOT_NULL
     * @param pageInfo NOT_NULL
     * @return pageInfo
     */
    private PageInfo<Solution> getSolutionListInEs(Integer problemId, Integer[] tags, String searchVal, PageInfo<Solution> pageInfo){
        //构建两个查询器，一个是必须包含的数据，一个是可包含可不包含的数据
        BoolQueryBuilder mustBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder shouldBuilder = QueryBuilders.boolQuery();

        //problemID作为特定题的题解，必须包含
        mustBuilder.must(QueryBuilders.matchQuery("pid",problemId));

        //如果含有标签，则必须符合所有标签
        if(Objects.nonNull(tags) && tags.length!=0){
            for(int i : tags){
                mustBuilder.must(QueryBuilders.matchQuery("tags",i));
            }
        }

        shouldBuilder.should(QueryBuilders.wildcardQuery("title","*"+searchVal+"*"));
        shouldBuilder.should(QueryBuilders.wildcardQuery("summary","*"+searchVal+"*"));

        shouldBuilder.should(QueryBuilders.multiMatchQuery(searchVal,"title","summary"));

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(mustBuilder.must(shouldBuilder));

        PageRequest pageRequest = PageRequest.of(pageInfo.getPageNumber(), pageInfo.getPageSize());

        //获取查询到的结果
        NativeSearchQuery query = nativeSearchQueryBuilder.withPageable(pageRequest).build();

        Page<SolutionSearch> page = esSolutionDao.search(query);
        //将查询到的结果加入列表
        List<SolutionSearch> content = page.getContent();

        if (pageInfo.getHasCount()){
            pageInfo.setTotalCount((int)page.getTotalElements());
        }

        List<Solution> data = content.stream().map(Solution::fromEs).collect(Collectors.toList());

        pageInfo.setData(data);

        return pageInfo;
    }

    @Subscribe
    private void onAddLike(LikeEvent event){
        if (event.getItemType()!= ItemType.SOLUTION){
            return;
        }
        log.info("received add like . ");
        redisTemplate.boundHashOps(SOLUTION_LIKE_HASH_KEY)
                .increment(event.getItemId().toString(),event.getType()== LikeType.LIKE?1:-1);
    }

    @Scheduled(cron="0/45 * *  * * ? ")
    public void sync(){
        log.info("start sync like count to db");
        String code = VeryCodeUtil.generateCode(8);
        Boolean res = redisTemplate.opsForValue().setIfAbsent(SOLUTION_LIKE_LOCK_KEY, code, 10, TimeUnit.SECONDS);
        if (res!=null && res){
            try{
                Map<Object, Object> entries = redisTemplate.boundHashOps(SOLUTION_LIKE_HASH_KEY).entries();
                if (entries==null) {
                    return;
                }
                entries.forEach((k,v)->{
                    Integer id = Integer.valueOf((String) k);
                    Integer num = (Integer) v;
                    if (num!=0){
                        log.info("sync solution {} add {}",id,num);
                        baseMapper.updateLike(id,num);
                        redisTemplate.boundHashOps(SOLUTION_LIKE_HASH_KEY).increment(k,-num);
                    }
                });
            }finally {
                if (code.equals(redisTemplate.opsForValue().get(SOLUTION_LIKE_LOCK_KEY))){
                    redisTemplate.delete(SOLUTION_LIKE_LOCK_KEY);
                }
            }
        }
    }

}
