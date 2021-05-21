package top.quezr.hqoj.dao.mapper;

import top.quezr.hqoj.entity.Record;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.quezr.hqoj.enums.JudgeStauts;

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
}
