package top.quezr.hqoj.service.impl;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.quezr.hqoj.entity.AddCoinLog;
import top.quezr.hqoj.enums.AddCoinReason;
import top.quezr.hqoj.dao.mapper.AddCoinLogMapper;
import top.quezr.hqoj.dao.mapper.UserMapper;
import top.quezr.hqoj.service.AddCoinLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.quezr.hqoj.util.event.AddSolutionEvent;
import top.quezr.hqoj.util.event.CenterEventBus;
import top.quezr.hqoj.util.event.UserLoginEvent;

import javax.annotation.PostConstruct;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author que
 * @since 2021-05-19
 */
@Service
@Slf4j
public class AddCoinLogServiceImpl extends ServiceImpl<AddCoinLogMapper, AddCoinLog> implements AddCoinLogService {

    @Autowired(required = false)
    UserMapper userMapper;

    @PostConstruct
    private void init(){
        CenterEventBus.bus.register(this);
    }

    @Subscribe
    public void userFirstLogin(UserLoginEvent event){
        log.info("user {} first login today . ",event.getUserId());
        addUserCoin(event.getUserId(),AddCoinReason.EVERYDAY_LOGIN.getConis());
        AddCoinLog log = new AddCoinLog();
        log.setUid(event.getUserId());
        log.setReason(AddCoinReason.EVERYDAY_LOGIN);
        baseMapper.insert(log);
    }

    @Subscribe
    public void userAddSolution(AddSolutionEvent event){
        log.info("user {} add solution.",event.getSolution().getUid());
        addUserCoin(event.getSolution().getUid(),AddCoinReason.ADD_SOLUTION.getConis());
        AddCoinLog log = new AddCoinLog();
        log.setUid(event.getSolution().getUid());
        log.setReason(AddCoinReason.ADD_SOLUTION);
        baseMapper.insert(log);
    }

    private void addUserCoin(Integer userId, int coins){
        userMapper.updateUserCoins(userId,coins);
    }

}
