package top.quezr.hqoj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 题目模块：用户通过记录
 * </p>
 *
 * @author que
 * @since 2021-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserPassed implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private Integer problemId;

    private Integer level;

    private LocalDateTime createTime;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


}
