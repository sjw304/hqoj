package top.quezr.hqoj.service.impl;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import top.quezr.hqoj.entity.Solution;
import top.quezr.hqoj.entity.Tag;
import top.quezr.hqoj.mapper.SolutionMapper;
import top.quezr.hqoj.service.SolutionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.quezr.hqoj.support.PageInfo;
import top.quezr.hqoj.support.Result;
import top.quezr.hqoj.util.event.AddSolutionEvent;
import top.quezr.hqoj.util.event.CenterEventBus;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author que
 * @since 2021-05-13
 */
@Service
public class SolutionServiceImpl extends ServiceImpl<SolutionMapper, Solution> implements SolutionService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public Result<PageInfo<Solution>> getSolutionPage(Integer problemId, Tag[] tags, String searchVal, PageInfo<Solution> pageInfo) {
        Result<PageInfo<Solution>> result = new Result<>();
        pageInfo = PageInfo.normalizing(pageInfo);

        String tagSearch = null;
        if (Objects.nonNull(tags) && tags.length>0){
            tagSearch = Arrays.toString(tags);
        }
        if (StrUtil.isBlank(searchVal)){
            List<Solution> solutions = baseMapper.getSolutionList(problemId,tagSearch,pageInfo.getPageSize(),pageInfo.getPageSize()*pageInfo.getPageNumber(),pageInfo.getLastId());
            pageInfo.setData(solutions);
            if (pageInfo.getHasCount()){
                Integer count = baseMapper.getSolutionListTotalCount(problemId,tagSearch);
                pageInfo.setTotalCount(count);
            }
            result.setData(pageInfo);
        } else {
            PageInfo<Solution> list = getSolutionListInEs(problemId, tags, searchVal, pageInfo);
            result.setData(list);
        }
        return result;
    }


    @Override
    public Result<Solution> getSolutionById(Integer id) {
        Result<Solution> result = new Result<>();
        Solution solution = baseMapper.selectById(id);
        if (Objects.isNull(solution)){
            result.setSuccess(false);
            result.setMessage("该题解不存在！");
            return result;
        }
        result.setData(solution);
        return result;
    }

    @Override
    public Result<Void> addSolution(Solution solution) {
        baseMapper.insert(solution);
        CenterEventBus.bus.post(new AddSolutionEvent(solution));
        return new Result<>();
    }

    /**
     * 通过es搜索题解
     * @param problemId NOT_NULL
     * @param tags NULL_ABLE
     * @param searchVal NOT_NULL
     * @param pageInfo NOT_NULL
     * @return pageInfo
     */
    private PageInfo<Solution> getSolutionListInEs(Integer problemId, Tag[] tags, String searchVal, PageInfo<Solution> pageInfo){
        return null;
    }
}
