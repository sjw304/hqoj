package xyz.lizhaorong.queoj.service;

import xyz.lizhaorong.queoj.entity.PageInfo;
import xyz.lizhaorong.queoj.entity.Problem;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.lizhaorong.queoj.entity.Result;

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

    Result<PageInfo<Problem>> getProblemList(Integer[] tags, String searchVal, Integer level, Integer pageSize, Integer pageNumber);

}
