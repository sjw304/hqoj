package top.quezr.hqoj.service.impl;

import com.google.common.eventbus.Subscribe;
import top.quezr.hqoj.entity.Message;
import top.quezr.hqoj.entity.Result;
import top.quezr.hqoj.enums.MessageType;
import top.quezr.hqoj.mapper.MessageMapper;
import top.quezr.hqoj.service.MessageService;
import top.quezr.hqoj.util.event.CenterEventBus;
import top.quezr.hqoj.util.event.UserRegisterEvent;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private static final String REGISTER_SUCCESS_TITLE = "欢迎来到hqoj！";
    private static final String REGISTER_SUCCESS_CONTENT = "欢迎您来到hqoj的世界，期待您与我们一起成长！点击题库页面开始你的旅程吧！";

    @PostConstruct
    private void init(){
        CenterEventBus.bus.register(this);
    }

    @Override
    public Result<Void> sendMessage(MessageType type, Integer receiverId, String title, String content) {
        Result<Void> result = new Result<>();
        baseMapper.sendMessage(type.getCode(),receiverId,title,content);
        return result;
    }

    @Subscribe
    public void sendRegisterMessage(UserRegisterEvent event){
        sendMessage(MessageType.SYSTEM_MESSAGE,event.getUser().getId(),REGISTER_SUCCESS_TITLE,REGISTER_SUCCESS_CONTENT);
    }
}
