package top.quezr.hqoj.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import top.quezr.hqoj.entity.User;
import top.quezr.hqoj.security.token.entity.TokenObject;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/11 21:21
 */
@Data
@ToString
@ApiModel("登录信息")
public class LoginResult {

    @ApiModelProperty("用户信息")
    private User info;

    @ApiModelProperty("token组")
    private TokenObject tokens;

}
