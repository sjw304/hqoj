package xyz.lizhaorong.queoj.service;

import xyz.lizhaorong.queoj.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.lizhaorong.queoj.entity.Result;
import xyz.lizhaorong.queoj.enums.MessageType;

import java.util.concurrent.CompletableFuture;

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
