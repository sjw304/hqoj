package top.quezr.hqoj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.quezr.hqoj.enums.JudgeStauts;
import top.quezr.hqoj.enums.LanguageType;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    @ApiModelProperty(value = "记录唯一识别id",example = "1")
    private Integer id;

    @ApiModelProperty(value = "对应的题目id",example = "1")
    private Integer pid;

    @ApiModelProperty(value = "提交的用户的id",example = "1")
    private Integer uid;

    private JudgeStauts state;

    @ApiModelProperty(value = "提交时间")
    private LocalDateTime time;

    @ApiModelProperty(value = "运行时间占用",example = "2123421")
    private Integer usedTime;

    @ApiModelProperty(value = "运行空间占用",example = "2123421")
    private Integer usedSpace;

    private LanguageType languageType;

    private String code;

    private String log;


}
