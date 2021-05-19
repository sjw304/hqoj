package top.quezr.hqoj.mapper;

import org.apache.ibatis.annotations.Param;
import top.quezr.hqoj.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
public interface MessageMapper extends BaseMapper<Message> {

    Integer sendMessage(@Param("type") int type,
                        @Param("receiverId") Integer receiverId,
                        @Param("title") String title,
                        @Param("content") String content);

    List<Message> selectMessagePage(@Param("userId") Integer level,
                           @Param("pageSize") Integer pageSize,
                           @Param("pageFrom") int pageFrom,
                           @Param("lastId") Integer lastId);

    Integer selectMessagePageTotalCount(Integer userId);

    Integer selectExistsNoReadMessage(Integer userId);

    Integer readMessage(Integer id);

    Integer readAllMessage(Integer userId);

    Integer deleteReadMessages(Integer userId);

    Integer unreadAllMessage(Integer userId);
}
