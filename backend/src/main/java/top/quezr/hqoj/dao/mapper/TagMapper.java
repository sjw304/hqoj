package top.quezr.hqoj.dao.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import top.quezr.hqoj.cache.RedisCache4Mybatis;
import top.quezr.hqoj.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
@CacheNamespace(implementation = RedisCache4Mybatis.class,eviction = RedisCache4Mybatis.class)
public interface TagMapper extends BaseMapper<Tag> {

}
