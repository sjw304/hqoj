package top.quezr.hqoj.service;

import top.quezr.hqoj.entity.Solution;
import com.baomidou.mybatisplus.extension.service.IService;
import top.quezr.hqoj.entity.Tag;
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
public interface SolutionService extends IService<Solution> {

    Result<PageInfo<Solution>> getSolutionPage(Integer problemId, Tag[] tags, String searchVal, PageInfo<Solution> pageInfo);

    Result<Solution> getSolutionById(Integer id);
}
