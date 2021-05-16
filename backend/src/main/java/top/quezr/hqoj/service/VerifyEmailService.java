package top.quezr.hqoj.service;

import top.quezr.hqoj.entity.Result;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/12 8:59
 */
public interface VerifyEmailService {

    String SEND_CODE_PREFIX = "email_sended:";
    // 一分钟服务调用限制时间
    int SEND_CODE_EXPIRE = 59;


    String CODE_PREFIX = "email_verify_code:";
    // 五分钟验证码过期时间
    int CODE_EXPIRE = 60*5;

    boolean generateCodeAndSendEmail(String address);


    Result<String> verifyEmailWithCode(String email, String verifyCode);
}
