package top.quezr.hqoj.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import top.quezr.hqoj.entity.DailyProblem;
import top.quezr.hqoj.dao.mapper.DailyProblemMapper;
import top.quezr.hqoj.dao.mapper.UserDailyMapper;
import top.quezr.hqoj.service.DailyProblemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.quezr.hqoj.service.ProblemService;
import top.quezr.hqoj.support.Result;
import top.quezr.hqoj.util.BinaryUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
public class DailyProblemServiceImpl extends ServiceImpl<DailyProblemMapper, DailyProblem> implements DailyProblemService {

    // opsForVal
    private static final String USER_DAILY_STATUS_KEY = "daily:%s:%s:";
    private static final String DAILY_PROBLEM_LIST_KEY = "dp:%s:";
    private static final String EMPTY_LIST_VALUE = "emp";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired(required = false)
    UserDailyMapper userDailyMapper;

    @Autowired
    ProblemService problemService;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Result<List<DailyProblem>> getDailyProblemNowMonth() {
        Result<List<DailyProblem>> result = new Result<>();
        List<DailyProblem> problems = getDailyProblemListFromRedisOrMysql();
        result.setData(problems);
        return result;
    }

    @Override
    public Result<List<Integer>> getUserDailyStatus(Integer userId) {
        Result<List<Integer>> result = new Result<>();

        LocalDate today = LocalDate.now();
        Integer status = getUserDailyStatusFromRedisOrMysql(userId,today);

        int count = today.getDayOfMonth();
        List<Integer> data = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            data.add(BinaryUtil.getStatusType(status,i));
        }
        result.setData(data);
        return result;
    }


    private List<DailyProblem> getDailyProblemListFromRedisOrMysql(){
        LocalDate today = LocalDate.now();
        LocalDate firstday = today.with(TemporalAdjusters.firstDayOfMonth());
        String str = (String) redisTemplate.opsForValue().get(String.format(DAILY_PROBLEM_LIST_KEY,today));
        List<DailyProblem> problems;
        if (Objects.isNull(str)){
            problems = baseMapper.selectDailyProblemList(today,firstday);
            if (problems==null){
                problems = List.of();
            }
            str = transformDailyProblemListToStr(problems);
            log.info("saved dailyProblems in redis.");
            redisTemplate.opsForValue().set(String.format(DAILY_PROBLEM_LIST_KEY,today),str,1,TimeUnit.DAYS);
        }else {
            problems = transformDailyProblemListFromStr(str);
        }
        return problems;
    }


    private Integer getUserDailyStatusFromRedisOrMysql(Integer userId,LocalDate today){
        LocalDate firstday = today.with(TemporalAdjusters.firstDayOfMonth());
        String key = String.format(USER_DAILY_STATUS_KEY,userId,firstday);
        String statusStr =  (String) redisTemplate.opsForValue().get(key);
        Integer status ;
        if (Objects.isNull(statusStr)){
            status = userDailyMapper.selectUserStatus(userId,firstday);
            status = status==null?0:status;
            redisTemplate.opsForValue().set(key,status.toString(),1, TimeUnit.DAYS);
        }else {
            status = Integer.valueOf(statusStr);
        }
        return status;
    }

    private List<DailyProblem> transformDailyProblemListFromStr(String str){
        if (EMPTY_LIST_VALUE.equals(str)){
            return List.of();
        }
        try {
            return objectMapper.readValue(str, TypeFactory.defaultInstance().constructCollectionType(ArrayList.class,DailyProblem.class));
        } catch (JsonProcessingException e) {
            log.error("error when transformDailyProblemListFromStr");
            return List.of();
        }
    }

    private String transformDailyProblemListToStr(List<DailyProblem> problems){
        if (problems.size()==0){
            return EMPTY_LIST_VALUE;
        }
        try {
            return objectMapper.writeValueAsString(problems);
        } catch (JsonProcessingException e) {
            return EMPTY_LIST_VALUE;
        }
    }

}
