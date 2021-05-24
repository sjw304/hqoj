package top.quezr.hqoj.service.impl;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import top.quezr.hqoj.dao.mapper.UserPassedMapper;
import top.quezr.hqoj.entity.LikeEvent;
import top.quezr.hqoj.entity.ProblemCount;
import top.quezr.hqoj.enums.ItemType;
import top.quezr.hqoj.enums.LikeType;
import top.quezr.hqoj.service.EsService;
import top.quezr.hqoj.support.PageInfo;
import top.quezr.hqoj.entity.Problem;
import top.quezr.hqoj.support.Result;
import top.quezr.hqoj.dao.mapper.ProblemMapper;
import top.quezr.hqoj.service.ProblemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.quezr.hqoj.util.VeryCodeUtil;
import top.quezr.hqoj.util.event.CenterEventBus;

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

    private static final String PRO_TIMES_KEY = "pro:times";
    private static final String PRO_REDIS_BREAK = "%%";
    private static final String PROBLEM_KEY_PREFIX = "pro:";
    private static final String EMPTY_PROBLEM_VALUE = "emp";
    private static final String PRO_LIST_KEY = "pro:f:list";
    private static final String PRO_LIST_COUNT_KEY = "pro:f:count";
    private static final String PRO_TOTAL_COUNT_KEY = "pro:total:count";
    private static final String PROBLEM_LIKE_HASH_KEY = "hash:count:p:";
    private static final String PROBLEM_LIKE_LOCK_KEY = "lock:count:p:";
    private static final String USER_PASSED_COUNT_KEY = "passed:count:%s:";

    /**
     * jackson 序列化工具
     */
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    UserPassedMapper userPassedMapper;

    @Autowired
    EsService esService;

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    BoundZSetOperations<String, String> zSetOps;

    @PostConstruct
    public void init() {
        zSetOps = redisTemplate.boundZSetOps(PRO_TIMES_KEY);
        objectMapper = new ObjectMapper();
        CenterEventBus.bus.register(this);
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
            // 首页，使用redis缓存
            log.debug("first page ");
            if (Objects.isNull(level) && Objects.isNull(tagSearch) && PageInfo.isFirstPage(pageInfo)){
                PageInfo<Problem> data = getFirstPage(pageInfo);
                result.setData(data);
                return result;
            }

            List<Problem> list = baseMapper.getProblemList(tagSearch,level,pageInfo.getPageSize(),pageInfo.getPageNumber()* pageInfo.getPageSize(),pageInfo.getLastId());
            pageInfo.setData(list);
            if (pageInfo.getHasCount()){
                Integer count = baseMapper.getProblemListTotalCount(tagSearch,level);
                pageInfo.setTotalCount(count);
            }
            result.setData(pageInfo);
            return result;
        }

        PageInfo<Problem> esPageInfo = esService.getProblemListInEs(tags==null?new Integer[]{}:tags,searchVal,level,pageInfo);
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

        Problem problem = getFromRedisOrMysql(id);
        if (Objects.isNull(problem)){
            result.setSuccess(false);
            result.setMessage("该题目不存在！");
            return result;
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

    @Override
    public Result<ProblemCount> getUserPassedCount(Integer userId) {
        Result<ProblemCount> result = new Result<>();

        String key = String.format(USER_PASSED_COUNT_KEY,userId);
        String countVal = redisTemplate.opsForValue().get(key);
        List<Integer> countList;
        if (Objects.isNull(countVal)){
            countList = userPassedMapper.selectUserPassedCount(userId);
            countVal =  countList.stream().map(String::valueOf).collect(Collectors.joining(","));
            redisTemplate.opsForValue().set(key,countVal,3,TimeUnit.DAYS);
        }else {
            countList = Arrays.stream(countVal.split(",")).map(Integer::valueOf).collect(Collectors.toList());
        }

        ProblemCount count = new ProblemCount();
        count.setEasy(countList.get(0));
        count.setMedium(countList.get(1));
        count.setHard(countList.get(2));
        result.setData(count);

        return result;
    }

    @Override
    public Result<ProblemCount> getTotalCount(){
        Result<ProblemCount> result = new Result<>();
        String key = PRO_TOTAL_COUNT_KEY;
        String countVal = redisTemplate.opsForValue().get(key);
        List<Integer> countList;
        if (Objects.isNull(countVal)){
            countList = baseMapper.getTotalCount();
            countVal =  countList.stream().map(String::valueOf).collect(Collectors.joining(","));
            redisTemplate.opsForValue().set(key,countVal,3,TimeUnit.DAYS);
        }else {
            countList = Arrays.stream(countVal.split(",")).map(Integer::valueOf).collect(Collectors.toList());
        }

        ProblemCount count = new ProblemCount();
        count.setEasy(countList.get(0));
        count.setMedium(countList.get(1));
        count.setHard(countList.get(2));
        result.setData(count);

        return result;
    }


    /**
     * 将数据缓存在redis
     * @param id
     * @return
     */
    private Problem getFromRedisOrMysql(Integer id){
        // 从redis获取数据
        String problemVal = redisTemplate.opsForValue().get(PROBLEM_KEY_PREFIX+id);
        Problem  problem;
        // 如果redis中为空
        if (Objects.isNull(problemVal)){
            // 从数据库中查数据
            problem = baseMapper.selectById(id);
            problemVal = transformProblemToJson(problem);
            log.debug("set problem {} values {} in redis.",id,problemVal);
            // 将题目信息暂存到redis
            redisTemplate.opsForValue().set(PROBLEM_KEY_PREFIX+id,problemVal,20, TimeUnit.MINUTES);
        }else {
            problem = transformJsonToProblem(problemVal);
        }
        return problem;
    }

    private PageInfo<Problem> getFirstPage(PageInfo<Problem> pageInfo) {
        // 从redis获取首屏数据
        List<String> list = redisTemplate.opsForList().range(PRO_LIST_KEY, 0, pageInfo.getPageSize());
        // 防止list为空
        list = Objects.isNull(list)?new ArrayList<>():list;
        int size = list.size();
        log.debug("size : {}",size);
        List<Problem> afterProblemList = null;
        // redis中数据不足
        if (size < pageInfo.getPageSize()){
            afterProblemList = baseMapper.getProblemList(null, null, pageInfo.getPageSize() - size, size, 0);
            List<String> problemVals = afterProblemList.stream().map(this::transformProblemToJson).collect(Collectors.toList());
            redisTemplate.opsForList().rightPushAll(PRO_LIST_KEY,problemVals);
        }

        List<Problem> problemList = list.stream().map(this::transformJsonToProblem).collect(Collectors.toList());
        if (Objects.nonNull(afterProblemList)){
            problemList.addAll(afterProblemList);
        }
        pageInfo.setData(problemList);

        if (pageInfo.getHasCount()){
            Integer count;
            String countVal = redisTemplate.opsForValue().get(PRO_LIST_COUNT_KEY);
            if (Objects.isNull(countVal)){
                count = baseMapper.getProblemListTotalCount(null,null);
                redisTemplate.opsForValue().set(PRO_LIST_COUNT_KEY,String.valueOf(count));
            }else {
                count = Integer.valueOf(countVal);
            }
            pageInfo.setTotalCount(count);
        }


        return pageInfo;
    }

    private String transformProblemToJson(Problem p){
        if (Objects.isNull(p)){
            return EMPTY_PROBLEM_VALUE;
        }
        try {
            return objectMapper.writeValueAsString(p);
        } catch (JsonProcessingException e) {
            log.error("[ getFromRedisOrMysql ] error in parse to json");
            return EMPTY_PROBLEM_VALUE;
        }
    }

    private Problem transformJsonToProblem(String problemVal){
        // 如果题目信息为默认空串
        if (EMPTY_PROBLEM_VALUE.equals(problemVal)){
            return  null;
        }else {
            try {
                return objectMapper.readValue(problemVal,Problem.class);
            } catch (JsonProcessingException e) {
                log.error("[ getFromRedisOrMysql ] error in parse from json");
                return null;
            }
        }
    }

    @Subscribe
    private void onAddLike(LikeEvent event){
        if (event.getItemType()!= ItemType.PROBLEM){
            return;
        }
        log.info("received add like . ");
        redisTemplate.boundHashOps(PROBLEM_LIKE_HASH_KEY)
                .increment(event.getItemId().toString(),event.getType()== LikeType.LIKE?1:-1);
    }

    @Scheduled(cron="*/60 * *  * * ? ")
    public void sync(){
        String code = VeryCodeUtil.generateCode(8);
        Boolean res = redisTemplate.opsForValue().setIfAbsent(PROBLEM_LIKE_LOCK_KEY, code, 10, TimeUnit.SECONDS);
        if (res!=null && res){
            try{
                Map<Object, Object> entries = redisTemplate.boundHashOps(PROBLEM_LIKE_HASH_KEY).entries();
                if (entries==null) {
                    return;
                }
                entries.forEach((k,v)->{
                    Integer id = Integer.valueOf((String) k);
                    Integer num = (Integer) v;
                    if (num!=0){
                        log.info("sync problem {} add {}",id,num);
                        baseMapper.updateLike(id,num);
                        // 不完善，会在redis中留下空洞
                        redisTemplate.boundHashOps(PROBLEM_LIKE_HASH_KEY).increment(k,-num);
                        // 有bug，可能会存在并发安全问题
                        // redisTemplate.boundHashOps(PROBLEM_LIKE_HASH_KEY).delete(k);
                    }
                });
            }finally {
                // 有bug，可能会存在并发安全问题
                if (code.equals(redisTemplate.opsForValue().get(PROBLEM_LIKE_LOCK_KEY))){
                    redisTemplate.delete(PROBLEM_LIKE_LOCK_KEY);
                }
            }
        }
    }
}
