package xyz.lizhaorong.queoj.entity;

import java.time.LocalDate;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserDaily implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer uid;

    /**
     * 月份，虽然是date，但是它只应包含年和月
     */
    private LocalDate month;

    /**
     * 一个int（32）位，从高位开始，每一位代表当前用户在当前月的答题情况
     */
    private Integer status;


}
