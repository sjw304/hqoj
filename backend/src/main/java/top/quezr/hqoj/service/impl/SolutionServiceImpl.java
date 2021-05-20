package top.quezr.hqoj.service.impl;

import cn.hutool.core.util.StrUtil;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import top.quezr.hqoj.entity.LikeEvent;
import top.quezr.hqoj.entity.Solution;
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
        return null;
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
