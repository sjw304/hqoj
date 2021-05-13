package xyz.lizhaorong.queoj.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/11 22:01
 */
public enum LanguageType {
    /**
     * 1 : 用户已离职
     * 0 : 正常状态
     */
    JAVA(0,"java"),
    CPP(1,"c/cpp"),
    PYTHON(2,"python")
    ;
    @EnumValue
    private final int code;
    private final String desp;


    LanguageType(int code, String desp) {
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

