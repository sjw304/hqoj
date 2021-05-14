package top.quezr.hqoj.service;

import top.quezr.hqoj.entity.Result;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/12 8:59
 */
public interface VerifyEmailService {

    boolean generateCodeAndSendEmail(String address);

    boolean isVerifyPassed(String emailAddress,String code);

    void removeVerifyPass(String emailAddress);

    Result<String> verifyEmailWithCode(String email, String verifyCode);
}
