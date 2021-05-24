package top.quezr.hqoj.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/11 22:01
 */
public enum AddCoinReason {
    /**
     *
     */
    DAILY_PROBLEM(1,"完成每日一题",10),
    ADD_SOLUTION(2,"发布题解",20),
    EVERYDAY_LOGIN(3,"每日登陆",3);

    @EnumValue
    private final int code;
    private final String desp;
    private final int conis;


    AddCoinReason(int code, String desp,int conis) {
        this.code = code;
        this.desp = desp;
        this.conis = conis;
    }

    public int getConis() {
        return conis;
    }

    public int getCode() {
        return code;
    }

    public String getDesp() {
        return desp;
    }
}

