package top.quezr.hqoj.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.quezr.hqoj.entity.Result;
import top.quezr.hqoj.service.UserLoginService;
import top.quezr.hqoj.support.Response;


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
