package top.quezr.hqoj.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import top.quezr.hqoj.entity.Favorite;
import top.quezr.hqoj.support.Result;
import top.quezr.hqoj.mapper.FavoriteMapper;
import top.quezr.hqoj.service.FavoriteService;
import top.quezr.hqoj.util.event.CenterEventBus;
import top.quezr.hqoj.util.event.UserRegisterEvent;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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
@Slf4j
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {


    private static final String DEFAULT_FAVORITE_NAME = "默认收藏夹";

    @PostConstruct
    private void init(){
        CenterEventBus.bus.register(this);
    }

    @Override
    public Result<List<Favorite>> getUserFavorites(Integer userId) {
        Result<List<Favorite>> result = new Result<>();
        List<Favorite> list = baseMapper.selectList(Wrappers.<Favorite>lambdaQuery().eq(Favorite::getUid, userId));
        result.setData(list);
        return result;
    }

    @Override
    public Result<Integer> addFavorite(Integer userId, String name) {
        Result<Integer> result = new Result<>();

        if (checkRepeatedName(userId,name,result)){
            return result;
        }

        Favorite favorite = new Favorite(null,userId,name);;
        baseMapper.insert(favorite);
        log.debug("genId :{}",favorite.getId());
        result.setData(favorite.getId());
        return result;
    }

    @Override
    public Result<Void> updateFavoriteName(Integer userId, String name, Integer favoriteId) {
        Result<Void> result = new Result<>();

        if (checkRepeatedName(userId,name,result)){
            return result;
        }

        Favorite favorite = baseMapper.selectById(favoriteId);
        if (Objects.isNull(favorite) || !Objects.equals(favorite.getUid(),userId)){
            result.setSuccess(false);
            result.setMessage("你不能操作这个收藏夹！");
            return result;
        }

        baseMapper.updateFavoriteName(favorite.getId(),name);
        return result;
    }

    @Subscribe
    private void createDefaultFavorite(UserRegisterEvent event){
        baseMapper.insert(new Favorite(null,event.getUser().getId(),DEFAULT_FAVORITE_NAME));
    }

    private boolean checkRepeatedName(Integer userId,String name,Result result){
        Favorite favorite = baseMapper.selectOne(Wrappers.<Favorite>lambdaQuery().eq(Favorite::getUid,userId).eq(Favorite::getName,name));
        if (Objects.nonNull(favorite)){
            result.setSuccess(false);
            result.setMessage("名称不能重复！");
            return true;
        }
        return false;
    }




}
