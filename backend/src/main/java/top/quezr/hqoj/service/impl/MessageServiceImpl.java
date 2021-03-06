package top.quezr.hqoj.service.impl;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import top.quezr.hqoj.entity.Message;
import top.quezr.hqoj.support.PageInfo;
import top.quezr.hqoj.support.Result;
import top.quezr.hqoj.enums.MessageType;
import top.quezr.hqoj.dao.mapper.MessageMapper;
import top.quezr.hqoj.service.MessageService;
import top.quezr.hqoj.util.event.AddSolutionEvent;
import top.quezr.hqoj.util.event.CenterEventBus;
import top.quezr.hqoj.util.event.UserRegisterEvent;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
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
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private static final String REGISTER_SUCCESS_TITLE = "欢迎来到hqoj！";
    private static final String REGISTER_SUCCESS_CONTENT = "欢迎您来到hqoj的世界，期待您与我们一起成长！点击题库页面开始你的旅程吧！";
    private static final String ADD_SOLUTION_TITLE = "发布题解成功！";
    private static final String ADD_SOLUTION_CONTENT = "您已成功发布题解 《%s》，奖励的硬币将会在几分钟内发到您的账户哦。";


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

    @Override
    public Result<PageInfo<Message>> getMessagePage(PageInfo<Message> pageInfo,Integer userId) {
        log.info("pageInfo : {}",pageInfo);
        pageInfo = PageInfo.normalizing(pageInfo);
        Result<PageInfo<Message>> result = new Result<>();
        List<Message> list = baseMapper.selectMessagePage(userId,  pageInfo.getPageSize(), pageInfo.getPageNumber()*pageInfo.getPageSize(), pageInfo.getLastId());
        pageInfo.setData(list);
        if (pageInfo.getHasCount()){
            Integer count = baseMapper.selectMessagePageTotalCount(userId);
            pageInfo.setTotalCount(count);
        }
        result.setData(pageInfo);
        return result;
    }

    @Override
    public Result<Boolean> getHasNoReadMessage(Integer id) {
        Result<Boolean> result = new Result<>();
        Integer exists = baseMapper.selectExistsNoReadMessage(id);
        result.setData(exists>0);
        return result;
    }

    @Override
    public void readMessage(Integer id) {
        baseMapper.readMessage(id);
    }

    @Override
    public void readAllMessage(Integer userId) {
        baseMapper.readAllMessage(userId);
    }

    @Override
    public void unreadAllMessage(Integer userId) {
        baseMapper.unreadAllMessage(userId);
    }

    @Override
    public Result<List<Message>> getAllNoReadMessages(Integer userId) {
        Result<List<Message>> result = new Result<>();
        List<Message> list =  baseMapper.selectAllNoReadMessages(userId);;
        result.setData(list==null?List.of():list);
        return result;
    }

    @Override
    public Result<Void> deleteMessage(Integer userId,Integer id) {
        Result<Void> result = new Result<>();
        Message message = baseMapper.selectById(id);
        if (Objects.isNull(message) || Objects.equals(message.getReceiverId(),userId)){
            result.setSuccess(false);
            result.setMessage("无权操作该条消息！");
        }
        baseMapper.deleteById(id);
        return result;
    }

    @Override
    public Result<Void> deleteReadMessages(Integer userId) {
        baseMapper.deleteReadMessages(userId);
        return new Result<>();
    }

    @Subscribe
    private void sendRegisterMessage(UserRegisterEvent event){
        sendMessage(MessageType.SYSTEM_MESSAGE,event.getUser().getId(),REGISTER_SUCCESS_TITLE,REGISTER_SUCCESS_CONTENT);
    }

    @Subscribe
    public void userAddSolution(AddSolutionEvent event){
        sendMessage(MessageType.SYSTEM_MESSAGE,event.getSolution().getUid(),ADD_SOLUTION_TITLE,String.format(ADD_SOLUTION_CONTENT,event.getSolution().getTitle()));
    }
}
