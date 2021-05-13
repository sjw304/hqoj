package xyz.lizhaorong.queoj.mapper;

import org.apache.ibatis.annotations.Param;
import xyz.lizhaorong.queoj.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
}
