package top.quezr.hqoj.dao.mapper;

import org.apache.ibatis.annotations.Param;
import top.quezr.hqoj.entity.DailyProblem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
public interface DailyProblemMapper extends BaseMapper<DailyProblem> {

    List<DailyProblem> selectDailyProblemList(@Param("today") LocalDate today,@Param("firstday") LocalDate firstday);
}
