package xyz.lizhaorong.queoj.controller.dto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/11 20:37
 */
@Data
@ToString
public class TagVo {

    @ApiModelProperty(value = "tagId",example = "1",required = false)
    private Integer id;

    @NotBlank
    @ApiModelProperty(value = "tag名",required = true)
    private String name;
}
