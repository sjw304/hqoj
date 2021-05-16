package top.quezr.hqoj.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.quezr.hqoj.entity.Result;
import top.quezr.hqoj.service.EmailService;
import top.quezr.hqoj.service.VerifyEmailService;
import top.quezr.hqoj.util.VeryCodeUtil;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * @author que
 * @version 1.0
 * @date 2021/3/19 13:00
 */
@Service
@Slf4j
public class VerifyEmailServiceImpl implements VerifyEmailService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 发送邮箱验证码
     * @param address 邮箱地址
     * @return 是否成功
     */
    @Override
    public boolean generateCodeAndSendEmail(String address){
        String code = VeryCodeUtil.generateCode(4);

        // 判断是否一分钟内重复发送
        String s = redisTemplate.opsForValue().get(SEND_CODE_PREFIX + address);
        log.debug("redis内存储的接口调用情况：{}",s);
        boolean exists = Objects.nonNull(s) ;
        if (exists){
            log.info("email地址{}在过期时间内(60s)重复调用接口。",address);
            return false;
        }

        redisTemplate.opsForValue()
                .set(SEND_CODE_PREFIX + address,"yes",SEND_CODE_EXPIRE,TimeUnit.SECONDS);

        redisTemplate.opsForValue()
                .set(CODE_PREFIX + address, code, CODE_EXPIRE, TimeUnit.SECONDS);

        try {
            // 异步发送邮件，不然太慢了！
            emailService.sendVerificationEmail(address,code);
        } catch (EmailException e) {
            e.printStackTrace();
            log.error("发送验证邮件失败");
            return false;
        }

        return true;

    }

    @Override
    public Result<String> verifyEmailWithCode(String emailAddress, String code){
        Result<String> result = new Result<>();
        String s = redisTemplate.opsForValue().get(CODE_PREFIX + emailAddress);
        log.debug("received email : {}",emailAddress);
        log.debug("key : {}",CODE_PREFIX + emailAddress);
        log.debug("received : {} , generated : {}",code,s);
        // 如果s不存在或不相同
        if (!code.equals(s)){
            result.setSuccess(false);
            result.setMessage("验证码错误或已失效");
            return result;
        }

        redisTemplate.delete(CODE_PREFIX+emailAddress);
        return result;
    }

}
