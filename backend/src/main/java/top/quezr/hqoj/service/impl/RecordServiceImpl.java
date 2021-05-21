package top.quezr.hqoj.service.impl;

import top.quezr.hqoj.entity.Record;
import top.quezr.hqoj.dao.mapper.RecordMapper;
import top.quezr.hqoj.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.quezr.hqoj.support.Result;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author que
 * @since 2021-05-13
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    @Override
    public Result<Void> submit(Record record) {
        return new Result<>();
    }
}
