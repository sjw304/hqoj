package top.quezr.hqoj.service;

import top.quezr.hqoj.entity.Message;
import top.quezr.hqoj.entity.Result;
import com.baomidou.mybatisplus.extension.service.IService;
import top.quezr.hqoj.enums.MessageType;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
public interface MessageService extends IService<Message> {

    Result<Void> sendMessage(MessageType type, Integer receiverId, String title, String content);

}
