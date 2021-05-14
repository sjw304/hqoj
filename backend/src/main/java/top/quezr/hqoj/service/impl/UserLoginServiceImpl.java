package top.quezr.hqoj.service.impl;

import top.quezr.hqoj.entity.UserLogin;
import top.quezr.hqoj.mapper.UserLoginMapper;
import top.quezr.hqoj.service.UserLoginService;
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
