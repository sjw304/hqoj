package top.quezr.hqoj.service;

import top.quezr.hqoj.entity.LikeEvent;
import com.baomidou.mybatisplus.extension.service.IService;
import top.quezr.hqoj.enums.ItemType;
import top.quezr.hqoj.support.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author que
 * @since 2021-05-18
 */
public interface LikeService extends IService<LikeEvent> {

    Result<Void> saveLikeEvent(LikeEvent event);

    Result<Boolean> isLiked(Integer userId, ItemType itemType, Integer itemId);
}
