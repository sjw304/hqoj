package top.quezr.hqoj.dao.mapper;

import org.apache.ibatis.annotations.Param;
import top.quezr.hqoj.entity.UserDaily;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.time.LocalDate;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
public interface UserDailyMapper extends BaseMapper<UserDaily> {

    Integer selectUserStatus(@Param("userId") Integer userId, @Param("first") LocalDate firstday);
}
