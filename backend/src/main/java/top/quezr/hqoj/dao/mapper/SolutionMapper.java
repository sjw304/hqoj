package top.quezr.hqoj.dao.mapper;

import org.apache.ibatis.annotations.Param;
import top.quezr.hqoj.entity.Solution;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author que
 * @since 2021-05-13
 */
public interface SolutionMapper extends BaseMapper<Solution> {

    List<Solution> getSolutionList(@Param("problemId") Integer problemId,
                                   @Param("tagSearch") String tagSearch,
                                   @Param("pageSize") Integer pageSize,
                                   @Param("pageFrom") Integer pageFrom,
                                   @Param("lastId") Integer lastId);

    Integer getSolutionListTotalCount(@Param("problemId") Integer problemId, @Param("tagSearch") String tagSearch);

    void updateLike(@Param("id") Integer id, @Param("num") Integer num);
}
