package xyz.lizhaorong.queoj.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import xyz.lizhaorong.queoj.controller.BaseController;
import xyz.lizhaorong.queoj.entity.Problem;
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
public class ProblemController extends BaseController {

    @Autowired
    ProblemService problemService;

    @GetMapping
    public Response<List<Problem>> getProblemList(Integer[] tags,String searchVal,Integer level,Integer pageSize,Integer pageNumber){
        problemService.getProblemList(tags,searchVal,level,pageSize,pageNumber);
        log.info("tags : {} ",tags);
        return new Response<>();
    }


}
