package top.quezr.hqoj.controller;


import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.quezr.hqoj.entity.Solution;
import top.quezr.hqoj.entity.Tag;
import top.quezr.hqoj.service.SolutionService;
import top.quezr.hqoj.support.PageInfo;
import top.quezr.hqoj.support.Response;
import top.quezr.hqoj.support.Result;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author que
 * @since 2021-05-13
 */
@RestController
@RequestMapping("/solution")
@Api(tags = "题解服务 Solution")
public class SolutionController extends BaseController {

    @Autowired
    SolutionService solutionService;

    @GetMapping
    public Response<PageInfo<Solution>> getSolutionPage(Integer problemId,
                                                        Tag[] tags,
                                                        String searchVal,
                                                        PageInfo<Solution> pageInfo){
        Result<PageInfo<Solution>> result = solutionService.getSolutionPage(problemId,tags,searchVal,pageInfo);
        return returnResult(result);
    }

    @GetMapping("/{id}")
    public Response<Solution> getSolution(@PathVariable("id") Integer id){
        Result<Solution> solution = solutionService.getSolutionById(id);
        return returnResult(solution);
    }

}
