package top.quezr.hqoj.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/12 9:48
 */
public enum JudgeStauts {
    /**
     * 判题状态
     */
    PENDING(0,"正在判题"),
    ACCEPT(1,"通过"),
    WRONG_ANSWER(2,"答案错误"),
    COMPILE_ERROR(3,"编译错误"),
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

    public static JudgeStauts of(int v) {
        switch (v){
            case 0:return PENDING;
            case 1:return ACCEPT;
            case 2:return WRONG_ANSWER;
            case 3:return COMPILE_ERROR;
            case 4:return TIME_LIMIT_EXCEED;
            case 5:return MEMORY_LIMIT_EXCEED;
            default:return RUNTIME_ERROR;
        }
    }

    public int getCode() {
        return code;
    }

    public String getDesp() {
        return desp;
    }
}
