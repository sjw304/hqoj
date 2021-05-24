package top.quezr.hqoj.dao.mapper;

import org.apache.ibatis.annotations.Param;
import top.quezr.hqoj.entity.UserPassed;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 题目模块：用户通过记录 Mapper 接口
 * </p>
 *
 * @author que
 * @since 2021-05-24
 */
public interface UserPassedMapper extends BaseMapper<UserPassed> {

    List<Integer> selectUserPassedCount(@Param("userId") Integer userId);



}
