package xyz.lizhaorong.queoj.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author que
 * @version 1.0
 * @date 2021/3/18 22:45
 */
@Configuration
@ConfigurationProperties(prefix = "smtp")
@Data
@ToString
public class SmtpConfig {
    private String host;
    private String username;
    private String password;
    private String senderName;
}
