package xyz.lizhaorong.queoj.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/11 20:35
 */
@Data
@ToString
public class Result<T> {
    private String message;
    private boolean success = true;
    private T data;
}
