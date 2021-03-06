package top.quezr.hqoj.service;

import top.quezr.hqoj.entity.ProblemCount;
import top.quezr.hqoj.support.PageInfo;
import top.quezr.hqoj.entity.Problem;
import top.quezr.hqoj.support.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
public interface ProblemService extends IService<Problem> {

    Result<PageInfo<Problem>> getProblemList(Integer[] tags, String searchVal, Integer level, PageInfo<Problem> pageInfo);

    Result<Problem> getProblemById(Integer id);

    Result<List<Problem>> getHotProblemList();

    Result<ProblemCount> getUserPassedCount(Integer userId);

    Result<ProblemCount> getTotalCount();
}
