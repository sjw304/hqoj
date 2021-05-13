package xyz.lizhaorong.queoj.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import xyz.lizhaorong.queoj.controller.BaseController;
import xyz.lizhaorong.queoj.entity.PageInfo;
import xyz.lizhaorong.queoj.entity.Problem;
import xyz.lizhaorong.queoj.entity.Result;
import xyz.lizhaorong.queoj.service.ProblemService;
import xyz.lizhaorong.queoj.support.Response;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
@RestController
@RequestMapping("/problem")
@Slf4j
@Api(tags = "题目信息")
public class ProblemController extends BaseController {

    @Autowired
    ProblemService problemService;

    @GetMapping
    @ApiOperation(value = "getProblemList",notes = "获取题目列表（分页）,只会返回如下字段：id, name, point, level, tags, `like`, unlike")
    public Response<List<Problem>> getProblemList(Integer[] tags, String searchVal, Integer level, PageInfo pageInfo){
        Result<List<Problem>> res = problemService.getProblemList(tags, searchVal, level, pageInfo);
        return returnResult(res);
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "getProblemById",notes = "用id，获取题目全部信息")
    public Response<Problem> getProblemById(@PathVariable("id") Integer id){
        Result<Problem> result = problemService.getProblemById(id);
        return returnResult(result);
    }

    @GetMapping("/hot")
    @ApiOperation(value = "getHotProblemList",notes = "获取热题榜，只包含id,name")
    public Response<List<Problem>> getHotProblemList(){
        Result<List<Problem>> result = problemService.getHotProblemList();
        return returnResult(result);
    }

}
