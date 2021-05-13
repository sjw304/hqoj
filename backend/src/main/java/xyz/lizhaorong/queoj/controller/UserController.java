package xyz.lizhaorong.queoj.controller;


import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import xyz.lizhaorong.queoj.controller.dto.LoginResult;
import xyz.lizhaorong.queoj.controller.dto.RegisterUserDto;
import xyz.lizhaorong.queoj.entity.Result;
import xyz.lizhaorong.queoj.entity.User;
import xyz.lizhaorong.queoj.service.UserService;
import xyz.lizhaorong.queoj.support.Response;

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

    @PostMapping("/login")
    public Response<LoginResult> login(String email, String password, HttpServletRequest request){
        log.info("user {} login .",email);
        Result<LoginResult> result = userService.login(email, password, request);
        return returnResult(result);
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册",notes = "注意是put方法。同一邮箱不能重复注册。注册时务必携带之前的8位验证码")
    public Response<Void> register(@Validated RegisterUserDto registerUserDto){
        String code = registerUserDto.getCode();
        User u =  new User();
        BeanUtils.copyProperties(registerUserDto,u);

        Result<Void> result = userService.register(u, code);
        return returnResult(result);
    }

    @GetMapping
    @ApiOperation(value = "获取用户信息")
    public Response<User> getUserInfo(Integer userId){
        Result<User> result = userService.getUserInfo(userId);
        return returnResult(result);
    }

}
