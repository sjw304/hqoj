package top.quezr.hqoj.service.impl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import top.quezr.hqoj.entity.PageInfo;
import top.quezr.hqoj.entity.Problem;
import top.quezr.hqoj.entity.Result;
import top.quezr.hqoj.mapper.ProblemMapper;
import top.quezr.hqoj.service.ProblemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
@Service
@Slf4j
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemService {

    private static final String PRO_TIMES_KEY = "proTimes";
    private static final String PRO_REDIS_BREAK = "%%";

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    BoundZSetOperations<String, String> zSetOps;

    @PostConstruct
    public void init() {
        zSetOps = redisTemplate.boundZSetOps(PRO_TIMES_KEY);
    }

    @Override
    public Result<PageInfo<Problem>> getProblemList(Integer[] tags, String searchVal, Integer level, PageInfo<Problem> pageInfo) {
        Result<PageInfo<Problem>> result = new Result<>();
        pageInfo = PageInfo.normalizing(pageInfo);

        String tagSearch = null;
        if (Objects.nonNull(tags) && tags.length>0){
            tagSearch = Arrays.toString(tags);
        }
        if (StrUtil.isBlank(searchVal)){
            List<Problem> list = baseMapper.getProblemList(tagSearch,level,pageInfo.getPageSize(),pageInfo.getPageNumber()* pageInfo.getPageSize(),pageInfo.getLastId());
            pageInfo.setData(list);
            if (pageInfo.getHasCount()){
                Integer count = baseMapper.getProblemListTotalCount(tagSearch,level);
                pageInfo.setTotalCount(count);
            }
            result.setData(pageInfo);
        }else {
            PageInfo<Problem> esPageInfo = getProblemListInEs(tags,searchVal,level,pageInfo);
            result.setData(esPageInfo);
        }
        return result;
    }

    @Override
    public Result<Problem> getProblemById(Integer id) {
        Result<Problem> result = new Result<>();
        if (Objects.isNull(id) || id<0){
            result.setSuccess(false);
            result.setMessage("非法id！");
            return result;
        }
        Problem problem = baseMapper.selectById(id);
        if (Objects.isNull(problem)){
            result.setSuccess(false);
            result.setMessage("该题目不存在！");
            return result;
        }
        zSetOps.incrementScore(problem.getName()+PRO_REDIS_BREAK+id,1);
        result.setData(problem);
        return result;
    }

    @Override
    public Result<List<Problem>> getHotProblemList() {
        Result<List<Problem>> result = new Result<>();
        Set<String> problemSet = zSetOps.reverseRange(0, 9);
        if (Objects.isNull(problemSet)){
            result.setSuccess(false);
            result.setMessage("获取热题榜失败，请稍后重试");
            return result;
        }
        List<Problem> problems = new ArrayList<>(problemSet.size());
        for (String problemString : problemSet) {
            Problem problem = new Problem();
            String[] arr = problemString.split(PRO_REDIS_BREAK);
            problem.setName(arr[0]);
            problem.setId(Integer.valueOf(arr[1]));
            problems.add(problem);
        }
        result.setData(problems);
        return result;
    }

    /**
     * 通过es搜索
     * @param tags EMPTY_ABLE
     * @param searchVal NOT_NULL
     * @param level NULL_ABLE
     * @param pageInfo NOT_NULL
     * @return
     */
    private PageInfo<Problem> getProblemListInEs(Integer[] tags, String searchVal, Integer level, PageInfo<Problem> pageInfo){
        return null;
    }
}
