package top.quezr.hqoj.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.quezr.hqoj.controller.dto.LoginResult;
import top.quezr.hqoj.controller.dto.RegisterUserDto;
import top.quezr.hqoj.entity.Result;
import top.quezr.hqoj.entity.User;
import top.quezr.hqoj.service.UserLoginService;
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
@RequestMapping("/userlogin")
@Slf4j
public class UserLoginController extends BaseController {

    @Autowired
    UserLoginService userLoginService;

    @PostMapping("/first")
    public Response<Void> userFirstLoginToday(Integer id){
        Result<Void> result = userLoginService.userFirstLogin(id);
        return returnResult(result);
    }

}
