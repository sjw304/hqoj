package top.quezr.hqoj.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;
import top.quezr.hqoj.controller.dto.LikeDto;
import top.quezr.hqoj.entity.LikeEvent;
import top.quezr.hqoj.enums.ItemType;
import top.quezr.hqoj.security.web.Authorization;
import top.quezr.hqoj.service.LikeService;
import top.quezr.hqoj.support.Response;
import top.quezr.hqoj.support.Result;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author que
 * @since 2021-05-18
 */
@RestController
@RequestMapping("/like")
@Api(tags = "点赞服务 Like")
public class LikeController extends BaseController {

    @Autowired
    LikeService likeService;

    @PutMapping
    @Authorization
    @ApiOperation("点赞/取消点赞")
    public Response<Void> like(@RequestAttribute(Authorization.USERID_ATTR) @ApiIgnore Integer userId,
                               @Validated LikeDto dto){
        LikeEvent event = new LikeEvent();
        event.setItemType(dto.getItemType());
        event.setType(dto.getType());
        event.setItemId(dto.getItemId());
        event.setUserId(userId);
        Result<Void> result =  likeService.saveLikeEvent(event);
        return returnResult(result);
    }

    @GetMapping
    @Authorization
    @ApiOperation("是否点赞了")
    public Response<Boolean> checkLike(@RequestAttribute(Authorization.USERID_ATTR) @ApiIgnore Integer userId,
                                    @Validated @NotNull @RequestParam ItemType itemType,@RequestParam @Validated @Min(1) Integer itemId){
        Result<Boolean> result =  likeService.isLiked(userId,itemType,itemId);
        return returnResult(result);
    }

}
