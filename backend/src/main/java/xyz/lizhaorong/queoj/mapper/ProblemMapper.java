package xyz.lizhaorong.queoj.mapper;

import org.apache.ibatis.annotations.Param;
import xyz.lizhaorong.queoj.entity.Problem;
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
public interface ProblemMapper extends BaseMapper<Problem> {

    List<Problem> getProblemList(@Param("tags") Integer[] tags,
                                 @Param("level") Integer level,
                                 @Param("pageSize") Integer pageSize,
                                 @Param("pageNumber") Integer pageNumber);
}
