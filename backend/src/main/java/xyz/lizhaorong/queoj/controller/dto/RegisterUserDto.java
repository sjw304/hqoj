package xyz.lizhaorong.queoj.controller.dto;

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
public class RegisterUserDto {

    @NotBlank
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$",message = "密码至少包含 数字和英文，长度6-20")
    @ApiModelProperty("密码至少包含 数字和英文，长度6-20")
    private String password;

    @Email
    @NotBlank
    @ApiModelProperty("邮箱地址")
    private String email;

    @NotBlank
    @ApiModelProperty("邮箱验证通过后的8位码")
    private String code;

}
