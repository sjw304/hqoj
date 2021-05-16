package top.quezr.hqoj.controller;


import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;
import top.quezr.hqoj.controller.dto.ChangePasswordDto;
import top.quezr.hqoj.controller.dto.LoginResult;
import top.quezr.hqoj.entity.Result;
import top.quezr.hqoj.entity.User;
import top.quezr.hqoj.security.token.entity.TokenObject;
import top.quezr.hqoj.security.web.Authorization;
import top.quezr.hqoj.service.UserService;
import top.quezr.hqoj.support.Response;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    @PostMapping("/login/password")
    @ApiOperation("使用密码登录")
    public Response<LoginResult> loginByPassword(String email, String password, HttpServletRequest request){
        Result<LoginResult> result = userService.loginByPassword(email, password, request);
        return returnResult(result);
    }

    @PostMapping("/login/code")
    @ApiOperation("使用验证码登录")
    public Response<LoginResult> loginByCode(String email, String code, HttpServletRequest request){
        Result<LoginResult> result = userService.loginByCode(email, code, request);
        return returnResult(result);
    }

    @GetMapping
    @ApiOperation(value = "获取用户信息")
    public Response<User> getUserInfo(Integer userId){
        Result<User> result = userService.getUserInfo(userId);
        return returnResult(result);
    }

    @PostMapping("/password")
    @ApiOperation("修改密码")
    @Authorization
    public Response<Void> changePassword(@RequestAttribute(Authorization.USERID_ATTR) @ApiIgnore Integer userId
                                         , @Validated ChangePasswordDto changePasswordDto){
        log.info("user {} changed password.",userId);
        Result<Void> result = userService.changePassword(userId, changePasswordDto.getOldPassword(), changePasswordDto.getNewPassword());
        return returnResult(result);
    }

    @PostMapping("/refresh")
    public Response<TokenObject> refresh(String token){
        Result<TokenObject> result = userService.refreshTokens(token);
        return returnResult(result);
    }
}
