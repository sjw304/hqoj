package top.quezr.hqoj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import top.quezr.hqoj.config.SmtpConfig;

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
@EnableCaching
@EnableConfigurationProperties(SmtpConfig.class)
@MapperScan("top.quezr.hqoj.dao.mapper")
public class HqojApplication {

    public static void main(String[] args) {
        SpringApplication.run(HqojApplication.class, args);
    }

}
