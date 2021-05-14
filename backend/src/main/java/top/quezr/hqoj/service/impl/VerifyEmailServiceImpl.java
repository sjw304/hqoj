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

    private static final String SEND_CODE_PREFIX = "email_sended:";
    // 一分钟服务调用限制时间
    private static final int SEND_CODE_EXPIRE = 59;


    private static final String CODE_PREFIX = "email_verify_code:";
    // 五分钟验证码过期时间
    private static final int CODE_EXPIRE = 60*5;

    private static final String VERIFY_PASSED_CODE = "passed_email_verify_code:";
    private static final int VERIFY_PASSED_EXPIRE = 30*60;


    /**
     * 发送邮箱验证码
     * @param address 邮箱地址
     * @return 是否成功
     */
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

        // 移除上一次的验证通过信息
        redisTemplate.delete(VERIFY_PASSED_CODE+address);

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

    public Result<String> verifyEmailWithCode(String emailAddress, String code){
        Result<String> result = new Result<>();
        String s = redisTemplate.opsForValue().get(CODE_PREFIX + emailAddress);
        log.info("received : {} , generated : {}",code,s);
        // 如果s不存在或不相同
        if (!code.equals(s)){
            result.setSuccess(false);
            result.setMessage("验证码错误或已失效");
            return result;
        }

        // 生成新的code
        String newCode = VeryCodeUtil.generateCode(8);

        redisTemplate.delete(CODE_PREFIX+emailAddress);
        // 如果已经验证过，则不重新设置。
        redisTemplate.opsForValue()
                .setIfAbsent(VERIFY_PASSED_CODE + emailAddress, newCode, VERIFY_PASSED_EXPIRE, TimeUnit.SECONDS);

        result.setData(newCode);
        return result;
    }

    public boolean isVerifyPassed(String emailAddress,String code){
        String oldCode = redisTemplate.opsForValue().get(VERIFY_PASSED_CODE + emailAddress);
        return code.equals(oldCode);
    }

    public void removeVerifyPass(String emailAddress){
        redisTemplate.delete(VERIFY_PASSED_CODE + emailAddress);
    }

}
