package top.quezr.hqoj.controller;


import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;
import top.quezr.hqoj.controller.dto.ChangePasswordDto;
import top.quezr.hqoj.controller.dto.ChangeUserInfoDto;
import top.quezr.hqoj.controller.dto.LoginResult;
import top.quezr.hqoj.support.Result;
import top.quezr.hqoj.entity.User;
import top.quezr.hqoj.security.token.entity.TokenObject;
import top.quezr.hqoj.security.web.Authorization;
import top.quezr.hqoj.service.UserLoginService;
import top.quezr.hqoj.service.UserService;
import top.quezr.hqoj.support.Response;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

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
@Api(tags = "用户服务 User")
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    @Autowired
    UserLoginService userLoginService;

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

    @PutMapping("/login/first")
    @ApiOperation("用户每日第一次登陆")
    @Authorization
    public Response<Void> userFirstLoginToday(@RequestAttribute(Authorization.USERID_ATTR) @ApiIgnore Integer userId){
        Result<Void> result = userLoginService.userFirstLogin(userId);
        return returnResult(result);
    }

    @GetMapping("/info")
    @ApiOperation(value = "获取自己的用户信息")
    @Authorization
    public Response<User> getMineInfo(@RequestAttribute(Authorization.USERID_ATTR) @ApiIgnore Integer userId){
        Result<User> result = userService.getUserInfo(userId);
        return returnResult(result);
    }

    @GetMapping("/info/{email}")
    @ApiOperation(value = "获取其他的用户信息")
    public Response<User> getUserInfo(@PathVariable("email") String email){
        Result<User> result = userService.getUserInfo(email);
        return returnResult(result);
    }

    @PutMapping("/info")
    @ApiOperation("修改信息")
    @Authorization
    public Response<Void> changeUserInfo(@RequestAttribute(Authorization.USERID_ATTR) @ApiIgnore Integer userId,@Validated ChangeUserInfoDto u){
        Response<Void> response = new Response<>();

        if (Objects.equals(u,ChangeUserInfoDto.EMPTY_DTO)){
            response.failure("不能为空");
            return response;
        }
        if (Objects.nonNull(u.getFavicon()) && StrUtil.isBlank(u.getFavicon())){
            response.failure("不能为空");
            return response;
        }
        if (Objects.nonNull(u.getNickname()) && StrUtil.isBlank(u.getNickname())){
            response.failure("不能为空");
            return response;
        }
        if (Objects.nonNull(u.getPhone()) && !Validator.isMobile(u.getPhone())){
            response.failure("手机号格式错误");
            return response;
        }
        if (Objects.nonNull(u.getIntroduction()) && StrUtil.isBlank(u.getIntroduction())){
            response.failure("不能为空");
            return response;
        }
        if (Objects.nonNull(u.getGithub()) && StrUtil.isBlank(u.getGithub())){
            if (!u.getGithub().startsWith("https://github.com/")){
                response.failure("github格式不正确");
                return response;
            }
        }
        if (Objects.nonNull(u.getWebsite()) && !Validator.isMatchRegex(PatternPool.URL_HTTP,u.getWebsite())){
            response.failure("网站格式不正确");
            return response;
        }
        if (Objects.nonNull(u.getWechat()) && StrUtil.isBlank(u.getWechat())){
            response.failure("不能为空");
            return response;
        }

        User user = new User();
        BeanUtils.copyProperties(u,user);
        user.setId(userId);
        Result<Void> result = userService.updateUserInfo(user);
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
    @ApiOperation("刷新token")
    public Response<TokenObject> refresh(String token){
        Result<TokenObject> result = userService.refreshTokens(token);
        return returnResult(result);
    }


}
