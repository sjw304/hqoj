package top.quezr.hqoj.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;
import top.quezr.hqoj.controller.dto.TagVo;
import top.quezr.hqoj.entity.Result;
import top.quezr.hqoj.entity.Tag;
import top.quezr.hqoj.security.web.Authorization;
import top.quezr.hqoj.service.TagService;
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
@RequestMapping("/tag")
@Api(tags = "标签服务 Tag")
@Slf4j
public class TagController extends BaseController {

    @Autowired
    TagService tagService;

    @GetMapping
    @ApiOperation("获取tags")
    @ApiImplicitParam(name = "ids" , value = "id列表" ,required = true ,paramType = "query")
    public Response<List<Tag>> getTagsByIds(@Validated @NonNull Integer[] ids){
        List<Tag> tags = tagService.getTagsByIds(ids);
        return success(tags);
    }

    @PutMapping
    @Authorization
    @ApiOperation("修改/创建tag")
    public Response<Void> setTag(@Validated TagVo tag, @ApiIgnore @RequestAttribute(Authorization.USERID_ATTR) String loginUserId){
        Tag tag1 = new Tag();
        BeanUtils.copyProperties(tag,tag1);
        log.debug("tag : {}",tag);
        Result<Void> result = tagService.setTag(tag1);
        return returnResult(result);
    }

}
