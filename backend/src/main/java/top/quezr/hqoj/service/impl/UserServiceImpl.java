package top.quezr.hqoj.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.quezr.hqoj.controller.dto.LoginResult;
import top.quezr.hqoj.support.Result;
import top.quezr.hqoj.entity.User;
import top.quezr.hqoj.mapper.UserMapper;
import top.quezr.hqoj.security.token.TokenManager;
import top.quezr.hqoj.security.token.entity.RefreshResult;
import top.quezr.hqoj.security.token.entity.SimpleUser;
import top.quezr.hqoj.security.token.entity.TokenObject;
import top.quezr.hqoj.service.FavoriteService;
import top.quezr.hqoj.service.MessageService;
import top.quezr.hqoj.service.UserService;
import top.quezr.hqoj.util.event.CenterEventBus;
import top.quezr.hqoj.util.event.UserLoginEvent;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.quezr.hqoj.service.VerifyEmailService;
import top.quezr.hqoj.util.event.UserRegisterEvent;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    TokenManager tokenManager;

    @Autowired
    VerifyEmailService verifyEmailService;

    @Autowired
    MessageService messageService;

    @Autowired
    FavoriteService favoriteService;

    private static final String EMPTY_PASSWORD = "lzrnb!!!";

    @PostConstruct
    private void init(){
        CenterEventBus.bus.register(this);
    }

    @Override
    public Result<LoginResult> loginByPassword(String email, String password, HttpServletRequest request) {
        Result<LoginResult> result = new Result<>();
        password = SecureUtil.md5(password);

        User u = baseMapper.getUserByEmal(email);
        if (u==null || !password.equals(u.getPassword())){
            result.setSuccess(false);
            result.setMessage("用户名或密码错误");
            return result;
        }

        LoginResult loginResult = new LoginResult();
        TokenObject tokenObject = generateToken(u,request);
        u.setPassword(null);
        loginResult.setInfo(u);
        loginResult.setTokens(tokenObject);

        result.setSuccess(true);
        result.setData(loginResult);
        return result;
    }

    @Override
    public Result<LoginResult> loginByCode(String email, String code, HttpServletRequest request) {
        Result<LoginResult> result = new Result<>();
        Result<String> verifyResult = verifyEmailService.verifyEmailWithCode(email, code);
        if (!verifyResult.isSuccess()){
            result.setMessage(verifyResult.getMessage());
            result.setSuccess(false);
            return result;
        }

        LoginResult loginResult = new LoginResult();

        User u = baseMapper.getUserByEmal(email);
        // 新用户
        if (Objects.isNull(u)) {
            baseMapper.register(email, EMPTY_PASSWORD);
            u = baseMapper.getUserByEmal(email);
            CenterEventBus.bus.post(new UserRegisterEvent(u));
            loginResult.setIsNewUser(true);
        }

        TokenObject tokenObject = generateToken(u,request);
        u.setPassword(null);
        loginResult.setInfo(u);
        loginResult.setTokens(tokenObject);
        result.setData(loginResult);
        return result;
    }

    @Override
    public Result<User> getUserInfo(Integer userId) {
        Result<User> result = new Result<>();
        User u = baseMapper.selectById(userId);
        if (Objects.isNull(u)) {
            result.setSuccess(false);
            result.setMessage("用户不存在");
            return result;
        }
        u.setPassword(null);
        result.setData(u);
        return result;
    }

    @Override
    public Result<User> getUserInfo(String email) {
        Result<User> result = new Result<>();
        User u = baseMapper.getUserByEmal(email);
        if (Objects.isNull(u)) {
            result.setSuccess(false);
            result.setMessage("用户不存在");
            return result;
        }
        u.setPassword(null);
        result.setData(u);
        return result;
    }

    @Override
    public Result<Void> changePassword(Integer userId,String oldPassword, String newPassword) {
        Result<Void> result = new Result<>();
        User u = baseMapper.selectById(userId);
        oldPassword = SecureUtil.md5(oldPassword);

        if (Objects.isNull(u)){
            result.setSuccess(false);
            result.setMessage("用户不存在！");
            return result;
        }

        if (u.getPassword().equals(oldPassword) || u.getPassword().equals(EMPTY_PASSWORD)){
            u.setPassword(SecureUtil.md5(newPassword));
            baseMapper.updateById(u);
            return result;
        }

        result.setSuccess(false);
        result.setMessage("旧密码错误！");
        return result;
    }

    @Override
    public Result<TokenObject> refreshTokens(String token) {
        Result<TokenObject> result = new Result<>();
        RefreshResult refresh = tokenManager.refresh(token);
        if (!refresh.isSuccess()){
            result.setMessage(refresh.getMessage());
            result.setSuccess(false);
            return result;
        }
        result.setData(refresh.getData());
        return result;
    }

    @Override
    public Result<Void> updateUserInfo(User u) {
        Result<Void> result = new Result<>();
        baseMapper.updateSelective(u);
        return result;
    }

    private void addUserCoin(Integer userId, int coins){
        baseMapper.updateUserCoins(userId,coins);
    }

    @Subscribe
    public void userFirstLogin(UserLoginEvent event){
        log.info("user {} first login today . ",event.getUserId());
        addUserCoin(event.getUserId(),1);
    }

    private TokenObject generateToken(User u, HttpServletRequest request){
        SimpleUser simpleUser = new SimpleUser(u.getId().toString(), ServletUtil.getClientIP(request),u.getRole().getCode(),0);
        return tokenManager.generate(simpleUser);
    }

}
