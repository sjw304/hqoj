package top.quezr.hqoj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/24 15:43
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class SearchService {
    public static void main(String[] args) {
        SpringApplication.run(SearchService.class);
    }
}
