package xyz.lizhaorong.queoj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import xyz.lizhaorong.queoj.enums.JudgeStauts;
import xyz.lizhaorong.queoj.enums.LanguageType;

/**
 * <p>
 *
 * </p>
 *
 * @author que
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer pid;

    private Integer uid;

    private JudgeStauts state;

    private LocalDateTime time;

    private Integer usedTime;

    private Integer usedSpace;

    private LanguageType languageType;

    private String code;


}
