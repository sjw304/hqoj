package xyz.lizhaorong.queoj.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author que
 * @version 1.0
 * @date 2021/3/19 16:11
 */
@Data
@ToString
@ApiModel
public class VerifyDto {

    @ApiModelProperty("邮箱地址")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    String email;

    @ApiModelProperty("验证码")
    @NotBlank(message = "验证码不能为空")
    String verifyCode;


}
