package top.quezr.hqoj.service;

import top.quezr.hqoj.entity.DailyProblem;
import com.baomidou.mybatisplus.extension.service.IService;
import top.quezr.hqoj.support.Result;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
public interface DailyProblemService extends IService<DailyProblem> {

    Result<List<DailyProblem>> getDailyProblemNowMonth();

    Result<List<Integer>> getUserDailyStatus(Integer userId);

}
