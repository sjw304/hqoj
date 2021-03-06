package top.quezr.hqoj.dao.mapper;

import org.apache.ibatis.annotations.Param;
import top.quezr.hqoj.entity.Problem;
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
//@CacheNamespace(implementation = RedisCache4Mybatis.class,eviction = RedisCache4Mybatis.class)
public interface ProblemMapper extends BaseMapper<Problem> {

    List<Problem> getProblemList(@Param("tagSearch") String tagSearch,
                                 @Param("level") Integer level,
                                 @Param("pageSize") Integer pageSize,
                                 @Param("pageFrom") Integer pageFrom,
                                 @Param("lastId") Integer lastId
    );

    Integer getProblemListTotalCount(@Param("tagSearch") String tagSearch,
                                     @Param("level") Integer level);

    Integer updateLike(@Param("id") Integer id, @Param("num") Integer num);

    List<Integer> getTotalCount();
}
