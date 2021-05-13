package xyz.lizhaorong.queoj.service.impl;

import xyz.lizhaorong.queoj.entity.FavoriteItem;
import xyz.lizhaorong.queoj.mapper.FavoriteItemMapper;
import xyz.lizhaorong.queoj.service.FavoriteItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
