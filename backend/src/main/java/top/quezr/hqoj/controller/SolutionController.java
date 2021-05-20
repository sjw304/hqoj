package top.quezr.hqoj.controller;


import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;
import top.quezr.hqoj.controller.dto.WriteSolutionDto;
import top.quezr.hqoj.entity.Solution;
import top.quezr.hqoj.entity.Tag;
import top.quezr.hqoj.security.web.Authorization;
import top.quezr.hqoj.service.SolutionService;
import top.quezr.hqoj.support.PageInfo;
import top.quezr.hqoj.support.Response;
import top.quezr.hqoj.support.Result;

import java.util.Arrays;
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
                                                        Integer[] tags,
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

    @PutMapping
    @Authorization
    public Response<Void> writeSolution(@Validated WriteSolutionDto solutionDto,@RequestAttribute(Authorization.USERID_ATTR) @ApiIgnore Integer userId){
        String tagVal = solutionDto.getTags()==null?"[]": Arrays.toString(solutionDto.getTags());
        Solution solution = new Solution();
        solution.setContent(solutionDto.getContent());
        solution.setPid(solutionDto.getPid());
        solution.setTags(tagVal);
        solution.setTitle(solutionDto.getTitle());
        int len = solutionDto.getContent().length();
        solution.setSummary(solutionDto.getContent().substring(0, Math.min(len, 100)));
        solution.setUid(userId);
        Result<Void> result = solutionService.addSolution(solution);
        return returnResult(result);
    }

}
