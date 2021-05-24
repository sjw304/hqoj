package top.quezr.hqoj.dao.mapper;

import org.apache.ibatis.annotations.Param;
import top.quezr.hqoj.entity.Record;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.quezr.hqoj.enums.JudgeStauts;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author que
 * @since 2021-05-13
 */
public interface RecordMapper extends BaseMapper<Record> {

    JudgeStauts selectStateById(Integer id);

    List<Record> selectRecordPage(@Param("userId") Integer userId,
                                  @Param("pageSize") Integer pageSize,
                                  @Param("pageFrom") int pageFrom,
                                  @Param("lastId") Integer lastId);

    Integer selectRecordPageTotalCount(@Param("userId") Integer userId);
}
