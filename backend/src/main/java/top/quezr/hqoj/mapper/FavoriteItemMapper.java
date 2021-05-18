package top.quezr.hqoj.mapper;

import top.quezr.hqoj.entity.FavoriteItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
public interface FavoriteItemMapper extends BaseMapper<FavoriteItem> {

    List<FavoriteItem> getFavoriteItems(Integer favoriteId);
}
