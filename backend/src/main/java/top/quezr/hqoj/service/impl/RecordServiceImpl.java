package top.quezr.hqoj.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import top.quezr.hqoj.entity.Record;
import top.quezr.hqoj.dao.mapper.RecordMapper;
import top.quezr.hqoj.enums.JudgeStauts;
import top.quezr.hqoj.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.quezr.hqoj.support.Result;

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
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    private static final String SUBMIT_TIME_LIMIT_KEY = "lim:sub:%s";
    private static final String SUBMIT_STATUS_KEY = "stu:sub:%s";
    private static final String EMPTY_STATUS = "-1";

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional
    public Result<Integer> submit(Record record) {
        Result<Integer> result = new Result<>();
        String key = String.format(SUBMIT_TIME_LIMIT_KEY,record.getUid());
        Boolean env = redisTemplate.opsForValue().setIfAbsent(key, "1", 2, TimeUnit.SECONDS);
        if (env!=null && env){
            // 利用rmq事务消息
            // rmq.presend

            baseMapper.insert(record);
            result.setData(record.getId());
            log.info("record id : {}",record.getId());
            String statusKey = String.format(SUBMIT_STATUS_KEY,record.getId());
            redisTemplate.opsForValue().set(statusKey,String.valueOf(record.getState().getCode()),30,TimeUnit.SECONDS);

            // rmq.commit
        }else {
            result.setSuccess(false);
            result.setMessage("请求过于频繁，请稍后重试");
        }
        return result;
    }

    @Override
    public Result<JudgeStauts> getStateById(Integer id) {
        Result<JudgeStauts> result = new Result<>();
        String key = String.format(SUBMIT_STATUS_KEY,id);
        String stateVal = redisTemplate.opsForValue().get(key);
        JudgeStauts state;
        if (stateVal==null){
            state = baseMapper.selectStateById(id);
            if (state!=null){
                redisTemplate.opsForValue().set(key,String.valueOf(state.getCode()),30,TimeUnit.SECONDS);
                result.setData(state);
            }else {
                redisTemplate.opsForValue().set(key,EMPTY_STATUS);
                result.setSuccess(false);
                result.setMessage("该提交不存在！");
            }
        }else{
            if (EMPTY_STATUS.equals(stateVal)){
                result.setSuccess(false);
                result.setMessage("该提交不存在！");
            }else {
                result.setData(JudgeStauts.of(Integer.parseInt(stateVal)));
            }
        }
        return result;
    }
}
