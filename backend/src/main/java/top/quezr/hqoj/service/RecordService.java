package top.quezr.hqoj.service;

import top.quezr.hqoj.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import top.quezr.hqoj.enums.JudgeStauts;
import top.quezr.hqoj.support.PageInfo;
import top.quezr.hqoj.support.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author que
 * @since 2021-05-13
 */
public interface RecordService extends IService<Record> {

    Result<Integer> submit(Record record);


    Result<JudgeStauts> getStateById(Integer id);

    Result<PageInfo<Record>> getUserRecordPage(Integer userId, PageInfo<Record> pageInfo);
}
