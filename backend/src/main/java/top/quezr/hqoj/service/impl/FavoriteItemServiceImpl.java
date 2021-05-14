package top.quezr.hqoj.service.impl;

import top.quezr.hqoj.entity.FavoriteItem;
import top.quezr.hqoj.mapper.FavoriteItemMapper;
import top.quezr.hqoj.service.FavoriteItemService;
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
