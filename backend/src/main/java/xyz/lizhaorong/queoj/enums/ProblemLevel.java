package xyz.lizhaorong.queoj.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/11 22:01
 */
public enum ProblemLevel {
    EASY(0,"简单"),
    MEDIUM(1,"中等"),
    HARD(2,"困难")
    ;
    @EnumValue
    private final int code;
    private final String desp;


    ProblemLevel(int code, String desp) {
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

