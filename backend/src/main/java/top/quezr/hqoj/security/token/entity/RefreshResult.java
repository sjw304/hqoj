package top.quezr.hqoj.security.token.entity;

import lombok.Data;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/16 20:25
 */
@Data
public class RefreshResult {
    private boolean success;
    private String message;
    private TokenObject data;
}
