package top.quezr.hqoj.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/11 22:01
 */
public enum ItemType {
    /**
     * 2 : 管理员
     * 1 : 普通用户
     */
    PROBLEM(0,"题目"),
    SOLUTION(1,"题解")
    ;
    @EnumValue
    private final int code;
    private final String desp;


    ItemType(int code, String desp) {
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

