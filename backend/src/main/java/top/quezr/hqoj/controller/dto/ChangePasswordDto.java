package top.quezr.hqoj.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/12 16:09
 */
@Data
@ToString
@ApiModel
public class ChangePasswordDto {

    @NotBlank
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$",message = "密码至少包含 数字和英文，长度6-20")
    @ApiModelProperty("新密码至少包含 数字和英文，长度6-20")
    private String newPassword;

    @ApiModelProperty("旧密码")
    private String oldPassword;

}
