package xyz.lizhaorong.queoj.service.impl;

import xyz.lizhaorong.queoj.entity.UserLogin;
import xyz.lizhaorong.queoj.mapper.UserLoginMapper;
import xyz.lizhaorong.queoj.service.UserLoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
@Service
public class UserLoginServiceImpl extends ServiceImpl<UserLoginMapper, UserLogin> implements UserLoginService {

}
