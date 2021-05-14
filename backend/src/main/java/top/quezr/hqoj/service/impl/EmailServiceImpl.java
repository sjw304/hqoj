package top.quezr.hqoj.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.EmailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.quezr.hqoj.config.SmtpConfig;
import top.quezr.hqoj.service.EmailService;

import java.util.Properties;

/**
 * @author que
 * @version 1.0
 * @date 2021/3/18 22:26
 */
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    SmtpConfig smtpConfig;

    private final JavaMailSenderImpl javaMailSender;

    private static final String VERIFICATION_PRE = "您好，您的验证码为： ";
    private static final String VERIFICATION_END = " 【bread网络】 ";

    public EmailServiceImpl(SmtpConfig smtpConfig){
        this.smtpConfig = smtpConfig;
        javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setUsername(smtpConfig.getUsername());
        javaMailSender.setHost(smtpConfig.getHost());
        javaMailSender.setPassword(smtpConfig.getPassword());
        // javaMailSender.setPort(587);
        javaMailSender.setPort(25);
        javaMailSender.setProtocol("smtp");
        javaMailSender.setDefaultEncoding("UTF-8");
        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", false);
        mailProperties.put("mail.smtp.starttls.enable", true);
        javaMailSender.setJavaMailProperties(mailProperties);
    }


    private void sendEmail(String emailAddress , String content , String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(smtpConfig.getUsername());
        message.setText(content);
        message.setSubject(subject);
        message.setTo(emailAddress);
        javaMailSender.send(message);
    }

    @Async
    public void sendVerificationEmail(String emailAddress , String code) throws EmailException {
        log.debug("开始发送邮件");
        sendEmail(emailAddress, VERIFICATION_PRE +code+VERIFICATION_END,"邮箱验证");
        log.debug("发送邮件完成");
    }

}
