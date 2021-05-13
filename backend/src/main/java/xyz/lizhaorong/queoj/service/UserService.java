package xyz.lizhaorong.queoj.service;

import xyz.lizhaorong.queoj.controller.dto.LoginResult;
import xyz.lizhaorong.queoj.entity.Result;
import xyz.lizhaorong.queoj.entity.User;
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
