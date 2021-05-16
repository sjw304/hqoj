package top.quezr.hqoj.service;

import top.quezr.hqoj.entity.Result;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/12 9:49
 */
public interface UserLoginService {

    Result<Void> userFirstLogin(Integer userId);

}
