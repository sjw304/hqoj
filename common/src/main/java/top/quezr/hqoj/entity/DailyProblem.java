package top.quezr.hqoj.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

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
public class DailyProblem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private LocalDate date;

    private Integer pid;

    @TableField(exist = false)
    private String problemName;


}
