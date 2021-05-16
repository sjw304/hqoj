package top.quezr.hqoj.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.ehcache.EhCacheCache;
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
@CacheNamespace
public interface ProblemMapper extends BaseMapper<Problem> {

    List<Problem> getProblemList(@Param("tagSearch") String tagSearch,
                                 @Param("level") Integer level,
                                 @Param("pageSize") Integer pageSize,
                                 @Param("pageNumber") Integer pageNumber,
                                 @Param("lastId") Integer lastId
    );

    Integer getProblemListTotalCount(@Param("tagSearch") String tagSearch,
                                     @Param("level") Integer level);
}
