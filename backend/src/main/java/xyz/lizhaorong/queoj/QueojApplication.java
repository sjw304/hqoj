package xyz.lizhaorong.queoj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import xyz.lizhaorong.queoj.config.SmtpConfig;

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@EnableConfigurationProperties(SmtpConfig.class)
@MapperScan("xyz.lizhaorong.queoj.mapper")
public class QueojApplication {

    public static void main(String[] args) {
        SpringApplication.run(QueojApplication.class, args);
    }

}
