package top.quezr.hqoj.service;

import top.quezr.hqoj.controller.dto.LoginResult;
import top.quezr.hqoj.entity.Result;
import top.quezr.hqoj.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import top.quezr.hqoj.security.token.entity.TokenObject;

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

    Result<LoginResult> loginByPassword(String email, String password, HttpServletRequest request);

    Result<LoginResult> loginByCode(String email, String code, HttpServletRequest request);

    Result<User> getUserInfo(Integer userId);

    Result<Void> changePassword(Integer userId,String oldPassword,String newPassword);

    Result<TokenObject> refreshTokens(String token);
}
