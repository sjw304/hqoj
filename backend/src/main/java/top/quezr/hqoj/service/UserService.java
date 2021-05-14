package top.quezr.hqoj.service;

import top.quezr.hqoj.controller.dto.LoginResult;
import top.quezr.hqoj.entity.Result;
import top.quezr.hqoj.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
public interface UserService extends IService<User> {

    Result<LoginResult> login(String email, String password, HttpServletRequest request);

    boolean existsByEmail(String email);

    Result<Void> register(User u, String code);

    Result<User> getUserInfo(Integer userId);
}
