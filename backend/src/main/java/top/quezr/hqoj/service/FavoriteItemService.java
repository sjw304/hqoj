package top.quezr.hqoj.service;

import top.quezr.hqoj.entity.FavoriteItem;
import com.baomidou.mybatisplus.extension.service.IService;
import top.quezr.hqoj.support.Result;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
public interface FavoriteItemService extends IService<FavoriteItem> {

    Result<List<FavoriteItem>> getFavoriteItems(Integer favoriteId);

    Result<Void> addFavoriteItem(Integer userId, FavoriteItem item);

    Result<Void> removeFavoriteItem(Integer userId, Integer itemId);
}
