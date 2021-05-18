package top.quezr.hqoj.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import top.quezr.hqoj.dao.esdao.EsProblemDao;
import top.quezr.hqoj.support.PageInfo;
import top.quezr.hqoj.entity.Problem;
import top.quezr.hqoj.entity.ProblemSearch;
import top.quezr.hqoj.support.Result;
import top.quezr.hqoj.mapper.ProblemMapper;
import top.quezr.hqoj.service.ProblemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
@Service
@Slf4j
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemService {

    private static final String PRO_TIMES_KEY = "proTimes";
    private static final String PRO_REDIS_BREAK = "%%";
    private static final String PROBLEM_KEY_PREFIX = "pro:";



    private final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    private EsProblemDao esProblemDao;

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    BoundZSetOperations<String, String> zSetOps;

    @PostConstruct
    public void init() {
        zSetOps = redisTemplate.boundZSetOps(PRO_TIMES_KEY);
    }

    @Override
    public Result<PageInfo<Problem>> getProblemList(Integer[] tags, String searchVal, Integer level, PageInfo<Problem> pageInfo) {
        Result<PageInfo<Problem>> result = new Result<>();
        pageInfo = PageInfo.normalizing(pageInfo);

        String tagSearch = null;
        if (Objects.nonNull(tags) && tags.length>0){
            tagSearch = Arrays.toString(tags);
        }

        if (StrUtil.isBlank(searchVal)){
            List<Problem> list = baseMapper.getProblemList(tagSearch,level,pageInfo.getPageSize(),pageInfo.getPageNumber()* pageInfo.getPageSize(),pageInfo.getLastId());
            pageInfo.setData(list);
            if (pageInfo.getHasCount()){
                Integer count = baseMapper.getProblemListTotalCount(tagSearch,level);
                pageInfo.setTotalCount(count);
            }
            result.setData(pageInfo);
            return result;
        }

        PageInfo<Problem> esPageInfo = getProblemListInEs(tags,searchVal,level,pageInfo);
        result.setData(esPageInfo);
        return result;
    }

    @Override
    public Result<Problem> getProblemById(Integer id) {
        Result<Problem> result = new Result<>();
        if (Objects.isNull(id) || id<0){
            result.setSuccess(false);
            result.setMessage("非法id！");
            return result;
        }
        String problemVal = redisTemplate.opsForValue().get(PROBLEM_KEY_PREFIX+id);
        Problem  problem;
        if (Objects.isNull(problemVal)){
            problem = baseMapper.selectById(id);
            if (Objects.isNull(problem)){
                result.setSuccess(false);
                result.setMessage("该题目不存在！");
                return result;
            }
            try {
                problemVal = objectMapper.writeValueAsString(problem);
            } catch (JsonProcessingException e) {
                result.setSuccess(false);
                result.setMessage("题目序列化错误！");
                return result;
            }
            log.debug("set problem {} in redis.",id);
            redisTemplate.opsForValue().set(PROBLEM_KEY_PREFIX+id,problemVal,1, TimeUnit.DAYS);
        }else {
            try {
                problem = objectMapper.readValue(problemVal,Problem.class);
            } catch (JsonProcessingException e) {
                result.setSuccess(false);
                result.setMessage("题目序列化错误！");
                return result;
            }
        }
        zSetOps.incrementScore(problem.getName()+PRO_REDIS_BREAK+id,1);
        result.setData(problem);
        return result;
    }

    @Override
    public Result<List<Problem>> getHotProblemList() {
        Result<List<Problem>> result = new Result<>();
        Set<String> problemSet = zSetOps.reverseRange(0, 9);
        if (Objects.isNull(problemSet)){
            result.setSuccess(false);
            result.setMessage("获取热题榜失败，请稍后重试");
            return result;
        }
        List<Problem> problems = new ArrayList<>(problemSet.size());
        for (String problemString : problemSet) {
            Problem problem = new Problem();
            String[] arr = problemString.split(PRO_REDIS_BREAK);
            problem.setName(arr[0]);
            problem.setId(Integer.valueOf(arr[1]));
            problems.add(problem);
        }
        result.setData(problems);
        return result;
    }

    /**
     * 通过es搜索题目
     * @param tags NULL_ABLE
     * @param searchVal NOT_NULL
     * @param level NULL_ABLE
     * @param pageInfo NOT_NULL
     * @return pageInfo
     */
    private PageInfo<Problem> getProblemListInEs(Integer[] tags, String searchVal, Integer level, PageInfo<Problem> pageInfo){


        //创造两个查询器，一个是必须包含的查询器，一个是至少包含一个的查询器
        //查询到的数据必定完全包含mustBuilder，只需要包含shouldBuilder中的一个即可
        BoolQueryBuilder mustBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder shouldBuilder = QueryBuilders.boolQuery();

        //判定tags是否为空，如果不为空则将tags中所有标签列入mustBuilder
        if(Objects.nonNull(tags) && tags.length!=0){
            for(int i : tags){
                mustBuilder.must(QueryBuilders.matchQuery("tags",i));
            }
        }

        //判定level是否为空，如果不为空则将level填入mustBuilder
        if(Objects.nonNull(level)){
            mustBuilder.must(QueryBuilders.matchQuery("level",level));
        }

        //判定标题、内容中是否含有searchVal，wildcardQuery是通配符查询，该意义为只要标题或内容分词后的词列表中包含searchVal则可以被查询到
        shouldBuilder.should(QueryBuilders.wildcardQuery("name", "*"+searchVal+"*"));
        shouldBuilder.should(QueryBuilders.wildcardQuery("description", "*"+searchVal+"*"));

        //由于wildcardQuery不会对searchVal进行分词，所以如果查找“两数和”是不会出现标题为“两数之和”的题目的
        //在这里进行能够将searchVal分词的查询条件
        shouldBuilder.should(QueryBuilders.multiMatchQuery(searchVal, "name","description"));

        //创建查询数据时使用的对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        //填入查询条件
        //must与should在同一个查询条件下，should会失效。所以创建两个builder，并将shouldBuild而设为mustBuilder的必须
        nativeSearchQueryBuilder.withQuery(mustBuilder.must(shouldBuilder));

        PageRequest pageRequest = PageRequest.of(pageInfo.getPageNumber(),pageInfo.getPageSize());

        //获取查询到的结果
        NativeSearchQuery query = nativeSearchQueryBuilder.withPageable(pageRequest).build();

        Page<ProblemSearch> page = esProblemDao.search(query);
        //将查询到的结果加入列表
        List<ProblemSearch> content = page.getContent();

        if (pageInfo.getHasCount()){
            pageInfo.setTotalCount((int)page.getTotalElements());
        }

        List<Problem> data = content.stream().map(Problem::fromEs).collect(Collectors.toList());

        pageInfo.setData(data);

        return pageInfo;
    }
}
