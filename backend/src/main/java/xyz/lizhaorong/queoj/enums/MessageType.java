package xyz.lizhaorong.queoj.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/12 9:48
 */
public enum MessageType {
    SYSTEM_MESSAGE(0,"系统通知"),
    LIKE_MESSAGE(1,"点赞通知");


    @EnumValue
    private final int code;
    private final String desp;

    MessageType(int code, String desp) {
        this.code = code;
        this.desp = desp;
    }

    public int getCode() {
        return code;
    }

    public String getDesp() {
        return desp;
    }
}
