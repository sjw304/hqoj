package top.quezr.hqoj.security.token.entity;

import lombok.Data;
import top.quezr.hqoj.support.ErrorCode;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/13 19:54
 */
@Data
public class CheckResult {

    boolean success;
    SimpleUser user;
    ErrorCode errorCode;

}
