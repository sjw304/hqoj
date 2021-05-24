package top.quezr.hqoj.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/11 22:01
 */
public enum LikeType {
    /**
     * 2 : 管理员
     * 1 : 普通用户
     */
    LIKE(0,"点赞"),
    UNDO_LIKE(1,"取消点赞"),
    ;
    @EnumValue
    private final int code;
    private final String desp;


    LikeType(int code, String desp) {
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

