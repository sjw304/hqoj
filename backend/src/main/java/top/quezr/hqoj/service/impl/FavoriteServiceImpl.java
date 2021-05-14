package top.quezr.hqoj.service.impl;

import com.google.common.eventbus.Subscribe;
import top.quezr.hqoj.entity.Favorite;
import top.quezr.hqoj.mapper.FavoriteMapper;
import top.quezr.hqoj.service.FavoriteService;
import top.quezr.hqoj.util.event.CenterEventBus;
import top.quezr.hqoj.util.event.UserRegisterEvent;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {


    private static final String DEFAULT_FAVORITE_NAME = "默认收藏夹";

    @PostConstruct
    private void init(){
        CenterEventBus.bus.register(this);
    }


    @Subscribe
    private void createDefaultFavorite(UserRegisterEvent event){
        baseMapper.insert(new Favorite(null,event.getUser().getId(),DEFAULT_FAVORITE_NAME));
    }
}
