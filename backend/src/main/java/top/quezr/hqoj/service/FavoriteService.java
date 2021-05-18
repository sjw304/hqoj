package top.quezr.hqoj.service;

import top.quezr.hqoj.entity.Favorite;
import com.baomidou.mybatisplus.extension.service.IService;
import top.quezr.hqoj.entity.Result;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
public interface FavoriteService extends IService<Favorite> {


    Result<List<Favorite>> getUserFavorites(Integer userId);

    Result<Integer> addFavorite(Integer userId, String name);

    Result<Void> updateFavoriteName(Integer userId, String name, Integer favoriteId);
}
