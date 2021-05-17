package top.quezr.hqoj.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import top.quezr.hqoj.entity.Result;
import top.quezr.hqoj.entity.UserLogin;
import top.quezr.hqoj.mapper.UserLoginMapper;
import top.quezr.hqoj.service.UserLoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.quezr.hqoj.util.event.CenterEventBus;
import top.quezr.hqoj.util.event.UserLoginEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
public class UserLoginServiceImpl extends ServiceImpl<UserLoginMapper, UserLogin> implements UserLoginService {

    private static final String USER_FIRST_LOGIN_PREFIX = "FUL_";

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public Result<Void> userFirstLogin(Integer userId) {
        Result<Void> result = new Result<>();
        if (Objects.isNull(userId)){
            result.setSuccess(false);
            result.setMessage("用户名不能为空");
            return result;
        }
        LocalDate today = LocalDate.now();
        String key = USER_FIRST_LOGIN_PREFIX+today.toString()+"_"+userId;
        String login = redisTemplate.opsForValue().get(key);
        if (Objects.nonNull(login)){
            log.debug("user {} had login.",userId);
            return result;
        }

        redisTemplate.opsForValue().set(key,"1",1, TimeUnit.DAYS);
        CenterEventBus.bus.post(new UserLoginEvent(userId));
        baseMapper.insert(new UserLogin(userId,LocalDateTime.now()));
        return result;
    }
}
