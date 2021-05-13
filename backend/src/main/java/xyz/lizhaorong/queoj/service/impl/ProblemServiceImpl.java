package xyz.lizhaorong.queoj.service.impl;

import cn.hutool.core.util.StrUtil;
import xyz.lizhaorong.queoj.entity.PageInfo;
import xyz.lizhaorong.queoj.entity.Problem;
import xyz.lizhaorong.queoj.entity.Result;
import xyz.lizhaorong.queoj.mapper.ProblemMapper;
import xyz.lizhaorong.queoj.service.ProblemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemService {

    @Override
    public Result<PageInfo<Problem>> getProblemList(Integer[] tags, String searchVal, Integer level, Integer pageSize, Integer pageNumber) {
        if (Objects.isNull(pageSize) || pageSize<1){
            pageSize = PageInfo.DEFAULT_PAGE_SIZE;
        }

        if (StrUtil.isBlank(searchVal)){
            List<Problem> result = baseMapper.getProblemList(tags,level,pageSize,pageNumber);
        }else {
            List<Problem> result = getProblemListInEs(tags,searchVal,level,pageSize,pageNumber);
        }
        return null;
    }

    /**
     * 通过es搜索
     * @param tags
     * @param searchVal NOT_NULL
     * @param level
     * @param pageSize
     * @param pageNumber
     * @return
     */
    private List<Problem> getProblemListInEs(Integer[] tags, String searchVal, Integer level, Integer pageSize, Integer pageNumber){
        return null;
    }
}
