package top.quezr.hqoj.service;

import top.quezr.hqoj.entity.Result;
import top.quezr.hqoj.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
public interface TagService extends IService<Tag> {

    List<Tag> getTagsByIds(Integer[] ids);

    Result<Void> setTag(Tag tag);
}
