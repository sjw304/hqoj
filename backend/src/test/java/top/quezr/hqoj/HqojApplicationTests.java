package top.quezr.hqoj;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.quezr.hqoj.config.SmtpConfig;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootTest
class HqojApplicationTests {

    @Autowired
    SmtpConfig smtpConfig;


    @Test
    public void testStmpConfig(){
        System.out.println(smtpConfig.getHost());
        System.out.println(smtpConfig.getUsername());
        System.out.println(smtpConfig.getSenderName());
        System.out.println(smtpConfig.getPassword());
    }

    public static void main(String[] args) {
        //System.out.println(SecureUtil.md5("123456"));
        LocalDate today = LocalDate.now();
        System.out.println(today);
    }
}
