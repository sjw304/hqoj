package top.quezr.hqoj;

import cn.hutool.core.lang.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.quezr.hqoj.config.SmtpConfig;
import top.quezr.hqoj.util.BinaryUtil;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;

@SpringBootTest
class HqojApplicationTests {


    public static void main(String[] args) {
        LocalDate firstday = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        System.out.println(firstday);
        System.out.println(LocalDate.now().getDayOfMonth());
    }
}
