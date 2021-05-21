package top.quezr.hqoj.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;
import top.quezr.hqoj.entity.DailyProblem;
import top.quezr.hqoj.enums.ProblemLevel;
import top.quezr.hqoj.security.web.Authorization;
import top.quezr.hqoj.service.DailyProblemService;
import top.quezr.hqoj.support.PageInfo;
import top.quezr.hqoj.entity.Problem;
import top.quezr.hqoj.support.Result;
import top.quezr.hqoj.service.ProblemService;
import top.quezr.hqoj.support.Response;

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
@Api(tags = "题目服务 Problem")
public class ProblemController extends BaseController {

    @Autowired
    ProblemService problemService;

    @Autowired
    DailyProblemService dailyProblemService;

    @GetMapping
    @ApiOperation(value = "getProblemList",notes = "获取题目列表（分页）,只会返回如下字段：id, name, point, level, tags")
    public Response<PageInfo<Problem>> getProblemList(Integer[] tags, String searchVal, ProblemLevel level, PageInfo<Problem> pageInfo){
        Result<PageInfo<Problem>> res = problemService.getProblemList(tags, searchVal, level==null?null:level.getCode(), pageInfo);
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

    @GetMapping("/daily/month")
    public Response<List<DailyProblem>> getDailyProblemNowMonth(){
        Result<List<DailyProblem>> result = dailyProblemService.getDailyProblemNowMonth();
        return returnResult(result);
    }

    @GetMapping("/daily/user-status")
    @Authorization
    public Response<List<Integer>> getUserDailyStatus(@RequestAttribute(Authorization.USERID_ATTR) @ApiIgnore Integer userId){
        Result<List<Integer>> result = dailyProblemService.getUserDailyStatus(userId);
        return returnResult(result);
    }

}
