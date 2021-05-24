package top.quezr.hqoj.controller;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.*;
import top.quezr.hqoj.dao.esdao.EsProblemDao;
import top.quezr.hqoj.dao.esdao.EsSolutionDao;
import top.quezr.hqoj.entity.Problem;
import top.quezr.hqoj.entity.ProblemSearch;
import top.quezr.hqoj.entity.Solution;
import top.quezr.hqoj.entity.SolutionSearch;
import top.quezr.hqoj.support.PageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/24 15:46
 */
@RestController
@RequestMapping("/es")
public class SearchController {

    @Autowired
    EsProblemDao esProblemDao;

    @Autowired
    EsSolutionDao esSolutionDao;

    /**
     * 通过es搜索题目
     * @param tags NULL_ABLE
     * @param searchVal NOT_NULL
     * @param level NULL_ABLE
     * @param pageInfo NOT_NULL
     * @return pageInfo
     */
    @GetMapping("/problem")
    PageInfo<Problem> getProblemListInEs(@RequestParam(required = false) Integer[] tags,@RequestParam String searchVal,@RequestParam(required = false) Integer level,@RequestBody PageInfo<Problem> pageInfo){
        //创造两个查询器，一个是必须包含的查询器，一个是至少包含一个的查询器
        //查询到的数据必定完全包含mustBuilder，只需要包含shouldBuilder中的一个即可
        BoolQueryBuilder mustBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder shouldBuilder = QueryBuilders.boolQuery();

        //判定tags是否为空，如果不为空则将tags中所有标签列入mustBuilder
        if(Objects.nonNull(tags) && tags.length!=0){
            for(int i : tags){
                mustBuilder.must(QueryBuilders.matchQuery("tags",i));
            }
        }

        //判定level是否为空，如果不为空则将level填入mustBuilder
        if(Objects.nonNull(level)){
            mustBuilder.must(QueryBuilders.matchQuery("level",level));
        }

        //判定标题、内容中是否含有searchVal，wildcardQuery是通配符查询，该意义为只要标题或内容分词后的词列表中包含searchVal则可以被查询到
        shouldBuilder.should(QueryBuilders.wildcardQuery("name", "*"+searchVal+"*"));
        shouldBuilder.should(QueryBuilders.wildcardQuery("description", "*"+searchVal+"*"));

        //由于wildcardQuery不会对searchVal进行分词，所以如果查找“两数和”是不会出现标题为“两数之和”的题目的
        //在这里进行能够将searchVal分词的查询条件
        shouldBuilder.should(QueryBuilders.multiMatchQuery(searchVal, "name","description"));

        //创建查询数据时使用的对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        //填入查询条件
        //must与should在同一个查询条件下，should会失效。所以创建两个builder，并将shouldBuild而设为mustBuilder的必须
        nativeSearchQueryBuilder.withQuery(mustBuilder.must(shouldBuilder));

        PageRequest pageRequest = PageRequest.of(pageInfo.getPageNumber(),pageInfo.getPageSize());

        //获取查询到的结果
        NativeSearchQuery query = nativeSearchQueryBuilder.withPageable(pageRequest).build();

        Page<ProblemSearch> page = esProblemDao.search(query);
        //将查询到的结果加入列表
        List<ProblemSearch> content = page.getContent();

        if (pageInfo.getHasCount()){
            pageInfo.setTotalCount((int)page.getTotalElements());
        }

        List<Problem> data = content.stream().map(ProblemSearch::fromEs).collect(Collectors.toList());

        pageInfo.setData(data);

        return pageInfo;
    }

    /**
     * 通过es搜索题解
     * @param problemId NOT_NULL
     * @param tags NULL_ABLE
     * @param searchVal NOT_NULL
     * @param pageInfo NOT_NULL
     * @return pageInfo
     */
    @GetMapping("/solution")
    private PageInfo<Solution> getSolutionListInEs(@RequestParam Integer problemId,@RequestParam(required = false) Integer[] tags,@RequestParam String searchVal,@RequestBody PageInfo<Solution> pageInfo){
        //构建两个查询器，一个是必须包含的数据，一个是可包含可不包含的数据
        BoolQueryBuilder mustBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder shouldBuilder = QueryBuilders.boolQuery();

        //problemID作为特定题的题解，必须包含
        mustBuilder.must(QueryBuilders.matchQuery("pid",problemId));

        //如果含有标签，则必须符合所有标签
        if(Objects.nonNull(tags) && tags.length!=0){
            for(int i : tags){
                mustBuilder.must(QueryBuilders.matchQuery("tags",i));
            }
        }

        shouldBuilder.should(QueryBuilders.wildcardQuery("title","*"+searchVal+"*"));
        shouldBuilder.should(QueryBuilders.wildcardQuery("summary","*"+searchVal+"*"));

        shouldBuilder.should(QueryBuilders.multiMatchQuery(searchVal,"title","summary"));

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(mustBuilder.must(shouldBuilder));

        PageRequest pageRequest = PageRequest.of(pageInfo.getPageNumber(), pageInfo.getPageSize());

        //获取查询到的结果
        NativeSearchQuery query = nativeSearchQueryBuilder.withPageable(pageRequest).build();

        Page<SolutionSearch> page = esSolutionDao.search(query);
        //将查询到的结果加入列表
        List<SolutionSearch> content = page.getContent();

        if (pageInfo.getHasCount()){
            pageInfo.setTotalCount((int)page.getTotalElements());
        }

        List<Solution> data = content.stream().map(SolutionSearch::fromEs).collect(Collectors.toList());

        pageInfo.setData(data);

        return pageInfo;
    }


}
