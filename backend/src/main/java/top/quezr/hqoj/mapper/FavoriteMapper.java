package top.quezr.hqoj.mapper;

import org.apache.ibatis.annotations.Param;
import top.quezr.hqoj.entity.Favorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
public interface FavoriteMapper extends BaseMapper<Favorite> {

    void updateFavoriteName(@Param("favoriteId") Integer favoriteId,@Param("name") String name);
}
