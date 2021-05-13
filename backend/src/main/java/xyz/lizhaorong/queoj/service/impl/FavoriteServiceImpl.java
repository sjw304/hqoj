package xyz.lizhaorong.queoj.service.impl;

import com.google.common.eventbus.Subscribe;
import xyz.lizhaorong.queoj.entity.Favorite;
import xyz.lizhaorong.queoj.entity.Result;
import xyz.lizhaorong.queoj.mapper.FavoriteMapper;
import xyz.lizhaorong.queoj.service.FavoriteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.lizhaorong.queoj.util.event.CenterEventBus;
import xyz.lizhaorong.queoj.util.event.UserRegisterEvent;

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
