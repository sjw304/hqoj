package top.quezr.hqoj.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.quezr.hqoj.controller.dto.VerifyDto;
import top.quezr.hqoj.entity.Result;
import top.quezr.hqoj.service.UserService;
import top.quezr.hqoj.service.VerifyEmailService;
import top.quezr.hqoj.support.Response;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author que
 * @version 1.0
 * @date 2021/3/18 22:06
 */
@RestController
@RequestMapping("/verify")
@Api(tags = "验证服务 Verify")
@Slf4j
public class VerifyController extends BaseController{

    @Autowired
    private VerifyEmailService verifyEmailService;

    @PutMapping("/email")
    @ApiOperation(value = "发送邮箱验证码",notes = "注意是put方法，该方法异步发送邮件，所以不保证一定发送成功。同一邮箱地址有时间间隔限制，为60秒。验证码五分钟内有效。")
    @ApiImplicitParam(name = "email" , value = "用户邮箱地址" ,required = true ,paramType = "query")
    public Response<Void> verifyEmail(@Validated @Email @NotBlank String email){
        Response<Void> response = new Response<>();

        boolean result = verifyEmailService.generateCodeAndSendEmail(email);
        if (result){
            return response.success();
        }

        return response.failure("发送email失败，请稍后重试");
    }

}
