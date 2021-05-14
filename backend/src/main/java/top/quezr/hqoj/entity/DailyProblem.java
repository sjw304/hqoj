package top.quezr.hqoj.entity;

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
public class DailyProblem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private LocalDate date;

    private Integer pid;


}
