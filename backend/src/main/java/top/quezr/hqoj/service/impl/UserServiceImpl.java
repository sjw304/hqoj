package top.quezr.hqoj.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.quezr.hqoj.controller.dto.LoginResult;
import top.quezr.hqoj.entity.Result;
import top.quezr.hqoj.entity.User;
import top.quezr.hqoj.mapper.UserMapper;
import top.quezr.hqoj.security.token.TokenManager;
import top.quezr.hqoj.security.token.entity.SimpleUser;
import top.quezr.hqoj.security.token.entity.TokenObject;
import top.quezr.hqoj.service.FavoriteService;
import top.quezr.hqoj.service.MessageService;
import top.quezr.hqoj.service.UserService;
import top.quezr.hqoj.util.event.CenterEventBus;
import top.quezr.hqoj.util.event.UserLoginEvent;
import top.quezr.hqoj.util.event.UserRegisterEvent;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.quezr.hqoj.service.VerifyEmailService;

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

    @PostConstruct
    private void init(){
        CenterEventBus.bus.register(this);
    }

    @Override
    public Result<LoginResult> login(String email, String password, HttpServletRequest request) {
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
    public Result<Void> register(User u, String code) {
        Result<Void> result = new Result<>();
        boolean passed = verifyEmailService.isVerifyPassed(u.getEmail(), code);
        if (!passed){
            result.setSuccess(false);
            result.setMessage("未身份验证或验证信息已过期！");
        }
        u.setPassword(SecureUtil.md5(u.getPassword()));

        baseMapper.register(u.getEmail(),u.getPassword());
        // 验证信息使用成功，移除验证信息
        verifyEmailService.removeVerifyPass(u.getEmail());

        User registered = baseMapper.getUserByEmal(u.getEmail());
        CenterEventBus.bus.post(new UserRegisterEvent(registered));
        return result;
    }

    @Override
    public boolean existsByEmail(String email) {
        return Objects.nonNull(baseMapper.getUserByEmal(email));
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

    private void addUserCoin(Integer userId,int coins){
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
