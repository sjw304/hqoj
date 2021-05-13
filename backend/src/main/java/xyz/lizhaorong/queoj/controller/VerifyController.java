package xyz.lizhaorong.queoj.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.lizhaorong.queoj.controller.dto.VerifyDto;
import xyz.lizhaorong.queoj.entity.Result;
import xyz.lizhaorong.queoj.entity.User;
import xyz.lizhaorong.queoj.service.UserService;
import xyz.lizhaorong.queoj.service.VerifyEmailService;
import xyz.lizhaorong.queoj.service.impl.VerifyEmailServiceImpl;
import xyz.lizhaorong.queoj.support.Response;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author que
 * @version 1.0
 * @date 2021/3/18 22:06
 */
@RestController
@RequestMapping("/verify")
@Api(tags = "验证服务")
@Slf4j
public class VerifyController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private VerifyEmailService verifyEmailService;

    @PutMapping("/email")
    @ApiOperation(value = "发送邮箱验证码",notes = "注意是put方法，该方法异步发送邮件，所以不保证一定发送成功。同一邮箱地址有时间间隔限制，为60秒。验证码五分钟内有效。")
    @ApiImplicitParam(name = "email" , value = "用户邮箱地址" ,required = true ,paramType = "query")
    public Response<Void> verifyEmail(@Validated @Email @NotBlank String email){
        Response<Void> response = new Response<>();

        boolean exists = userService.existsByEmail(email);
        if (exists){
            return response.failure("该email已注册！");
        }

        boolean result = verifyEmailService.generateCodeAndSendEmail(email);
        if (result){
            return response.success();
        }

        return response.failure("发送email失败，请稍后重试");
    }

    @PostMapping("/email")
    @ApiOperation(value = "检查验证码是否正确",notes = "提交之后将不能再次提交。成功后将返回一个新的8位验证码，之后调用下一步注册请求需要用到该验证码")
    public Response<String> verify(@Validated VerifyDto verifyDto){
        Result<String> result = verifyEmailService.verifyEmailWithCode(verifyDto.getEmail(), verifyDto.getVerifyCode());
        return returnResult(result);
    }

}
