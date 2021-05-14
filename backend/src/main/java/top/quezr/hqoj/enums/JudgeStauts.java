package top.quezr.hqoj.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/12 9:48
 */
public enum JudgeStauts {
    PENDING(0,"系统通知"),
    ACCEPT(1,"点赞通知"),
    WRONG_ANSWER(2,"点赞通知"),
    COMPILE_ERROR(3,"点赞通知"),
    TIME_LIMIT_EXCEED(4,"超过时间限制"),
    MEMORY_LIMIT_EXCEED(5,"超过空间限制"),
    RUNTIME_ERROR(6,"运行时错误");


    @EnumValue
    private final int code;
    private final String desp;

    JudgeStauts(int code, String desp) {
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
