package xyz.lizhaorong.queoj.service.impl;

import com.google.common.eventbus.Subscribe;
import org.springframework.scheduling.annotation.Async;
import xyz.lizhaorong.queoj.entity.Message;
import xyz.lizhaorong.queoj.entity.Result;
import xyz.lizhaorong.queoj.enums.MessageType;
import xyz.lizhaorong.queoj.mapper.MessageMapper;
import xyz.lizhaorong.queoj.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.lizhaorong.queoj.util.event.CenterEventBus;
import xyz.lizhaorong.queoj.util.event.UserRegisterEvent;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

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

    private static final String REGISTER_SUCCESS_TITLE = "欢迎来到queoj！";
    private static final String REGISTER_SUCCESS_CONTENT = "欢迎您来到queoj的世界，期待您与我们一起成长！点击题库页面开始你的旅程吧！";

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
