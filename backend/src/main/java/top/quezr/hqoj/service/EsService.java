package top.quezr.hqoj.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.quezr.hqoj.entity.Problem;
import top.quezr.hqoj.entity.Solution;
import top.quezr.hqoj.support.PageInfo;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/24 16:29
 */
@Component
@FeignClient("search-service")
@RequestMapping("/es")
public interface EsService {
    @GetMapping("/problem")
    PageInfo<Problem> getProblemListInEs(@RequestParam Integer[] tags, @RequestParam String searchVal, @RequestParam Integer level, @RequestBody PageInfo<Problem> pageInfo);

    @GetMapping("/solution")
    PageInfo<Solution> getSolutionListInEs(@RequestParam Integer problemId, @RequestParam Integer[] tags, @RequestParam String searchVal, @RequestBody PageInfo<Solution> pageInfo);
}
