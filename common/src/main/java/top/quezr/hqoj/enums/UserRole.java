package top.quezr.hqoj.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/11 22:01
 */
public enum UserRole {
    /**
     * 2 : 管理员
     * 1 : 普通用户
     */
    ADMIN(2,"管理员"),
    NORMAL(1,"普通用户")
    ;
    @EnumValue
    private final int code;
    private final String desp;


    UserRole(int code, String desp) {
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

