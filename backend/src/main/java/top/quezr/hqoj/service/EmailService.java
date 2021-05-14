package top.quezr.hqoj.service;

import org.apache.commons.mail.EmailException;
import org.springframework.scheduling.annotation.Async;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/12 9:00
 */
public interface EmailService {

    public void sendVerificationEmail(String emailAddress , String code) throws EmailException;
}
