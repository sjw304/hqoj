package top.quezr.hqoj.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import top.quezr.hqoj.entity.Favorite;
import top.quezr.hqoj.entity.FavoriteItem;
import top.quezr.hqoj.support.Result;
import top.quezr.hqoj.dao.mapper.FavoriteItemMapper;
import top.quezr.hqoj.dao.mapper.FavoriteMapper;
import top.quezr.hqoj.service.FavoriteItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
@Service
public class FavoriteItemServiceImpl extends ServiceImpl<FavoriteItemMapper, FavoriteItem> implements FavoriteItemService {

    @Autowired(required = false)
    FavoriteMapper favoriteMapper;

    @Override
    public Result<List<FavoriteItem>> getFavoriteItems(Integer favoriteId) {
        Result<List<FavoriteItem>> result = new Result<>();
        result.setData(baseMapper.getFavoriteItems(favoriteId));
        return result;
    }

    @Override
    public Result<Void> addFavoriteItem(Integer userId, FavoriteItem item) {
        Result<Void> result = new Result<>();
        Favorite favorite = favoriteMapper.selectById(item.getFavritesId());
        if (Objects.isNull(favorite) || !Objects.equals(favorite.getUid(),userId)){
            result.setSuccess(false);
            result.setMessage("你不能操作这个收藏夹！");
            return result;
        }

        FavoriteItem one = baseMapper.selectOne(Wrappers.<FavoriteItem>lambdaQuery()
                .eq(FavoriteItem::getFavritesId, item.getFavritesId())
                .eq(FavoriteItem::getItemId, item.getItemId())
                .eq(FavoriteItem::getType, item.getType())
        );

        if (Objects.nonNull(one)){
            result.setSuccess(false);
            result.setMessage("不能重复收藏！");
            return result;
        }

        baseMapper.insert(item);
        return result;
    }

    @Override
    public Result<Void> removeFavoriteItem(Integer userId, Integer itemId) {
        Result<Void> result = new Result<>();
        FavoriteItem item = baseMapper.selectById(itemId);
        if (Objects.isNull(item)){
            result.setSuccess(false);
            result.setMessage("该项不存在！");
            return result;
        }

        Favorite favorite = favoriteMapper.selectById(item.getFavritesId());
        if (Objects.isNull(favorite) || !Objects.equals(favorite.getUid(),userId)){
            result.setSuccess(false);
            result.setMessage("你不能操作这个收藏夹！");
            return result;
        }

        baseMapper.deleteById(itemId);
        return result;
    }
}
