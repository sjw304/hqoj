package top.quezr.hqoj.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;
import top.quezr.hqoj.controller.dto.AddFavoriteItemDto;
import top.quezr.hqoj.entity.Favorite;
import top.quezr.hqoj.entity.FavoriteItem;
import top.quezr.hqoj.support.Result;
import top.quezr.hqoj.security.web.Authorization;
import top.quezr.hqoj.service.FavoriteItemService;
import top.quezr.hqoj.service.FavoriteService;
import top.quezr.hqoj.support.Response;

import javax.validation.constraints.NotBlank;
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
@RequestMapping("/favorite")
@Api(tags = "收藏服务 Favorite")
public class FavoriteController extends BaseController {

    @Autowired
    FavoriteService favoriteService;

    @Autowired
    FavoriteItemService favoriteItemService;

    @GetMapping
    @ApiOperation("获取用户的收藏夹列表")
    public Response<List<Favorite>> getFavoriteList(Integer userId){
        Result<List<Favorite>> result = favoriteService.getUserFavorites(userId);
        return returnResult(result);
    }

    @PutMapping
    @Authorization
    @ApiOperation(value = "新增",notes = "不能重名")
    public Response<Integer> addFavorite(@RequestAttribute(Authorization.USERID_ATTR) @ApiIgnore Integer userId,
                                      @Validated @NotBlank @RequestParam String name){
        Result<Integer> result = favoriteService.addFavorite(userId,name);
        return returnResult(result);
    }

    @PostMapping
    @Authorization
    @ApiOperation(value = "修改收藏夹名称",notes = "不能重名，只能修改自己的收藏夹")
    public Response<Void> renameFavorite(@RequestAttribute(Authorization.USERID_ATTR) @ApiIgnore Integer userId,
                                         @Validated @NotBlank @RequestParam String name,
                                         Integer favoriteId){
        Result<Void> result = favoriteService.updateFavoriteName(userId,name,favoriteId);
        return returnResult(result);
    }

    @GetMapping("/item")
    @ApiOperation("获取收藏夹下的所有内容")
    public Response<List<FavoriteItem>> getFavoriteItems(Integer favoriteId){
        Result<List<FavoriteItem>> result =  favoriteItemService.getFavoriteItems(favoriteId);
        return returnResult(result);
    }

    @PutMapping("/item")
    @Authorization
    @ApiOperation("收藏某一题目/题解")
    public Response<Void> addFavoriteItem(@RequestAttribute(Authorization.USERID_ATTR) @ApiIgnore Integer userId,
                                                        AddFavoriteItemDto favoriteItemDto){
        FavoriteItem item = new FavoriteItem();
        item.setFavritesId(favoriteItemDto.getFavritesId());
        item.setType(favoriteItemDto.getType());
        item.setItemId(favoriteItemDto.getItemId());
        Result<Void> result =  favoriteItemService.addFavoriteItem(userId,item);
        return returnResult(result);
    }

    @DeleteMapping("/item")
    @Authorization
    @ApiOperation("删除收藏夹中的某一项")
    public Response<Void> removeFavoriteItem(@RequestAttribute(Authorization.USERID_ATTR) @ApiIgnore Integer userId,Integer favoriteItemId){
        Result<Void> result = favoriteItemService.removeFavoriteItem(userId,favoriteItemId);
        return returnResult(result);
    }

}
