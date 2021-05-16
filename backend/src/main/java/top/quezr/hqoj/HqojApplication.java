package top.quezr.hqoj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import top.quezr.hqoj.config.SmtpConfig;

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties(SmtpConfig.class)
@MapperScan("top.quezr.hqoj.mapper")
public class HqojApplication {

    public static void main(String[] args) {
        SpringApplication.run(HqojApplication.class, args);
    }

}
