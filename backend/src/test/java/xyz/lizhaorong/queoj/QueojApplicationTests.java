package xyz.lizhaorong.queoj;

import cn.hutool.crypto.SecureUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.lizhaorong.queoj.config.SmtpConfig;

import java.util.Arrays;

@SpringBootTest
class QueojApplicationTests {

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
        Integer[] arr = {1,2,3};
        System.out.println(Arrays.toString(arr));
    }
}
