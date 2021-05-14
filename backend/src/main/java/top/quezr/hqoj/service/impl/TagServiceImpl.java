package top.quezr.hqoj.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import top.quezr.hqoj.entity.Result;
import top.quezr.hqoj.entity.Tag;
import top.quezr.hqoj.mapper.TagMapper;
import top.quezr.hqoj.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    BoundHashOperations<String, Object, Object> tagMap;

    private static final String TAG_REDIS_KEY_PREFIX = "tagHash";

    private static final String EMPTY_TAG = "emp";

    @PostConstruct
    private void init(){
        tagMap = redisTemplate.boundHashOps(TAG_REDIS_KEY_PREFIX);
    }

    @Override
    public List<Tag> getTagsByIds(Integer[] ids) {
        if (ids==null || ids.length==0)return new ArrayList<>();
        Map<Object, Object> map = tagMap.entries();
        List<Tag> result = new ArrayList<>(ids.length);
        for (Integer id : ids) {
            String name = (String) map.get(String.valueOf(id));
            log.debug("name : {}",name);
            if (Objects.isNull(name)){
                Tag t = baseMapper.selectById(id);
                if (Objects.isNull(t)){
                    setTagEmptyInRedis(id);
                }else {
                    setTagExistsInRedis(id,t.getName());
                    result.add(t);
                }
                continue;
            }
            if (!EMPTY_TAG.equals(name)){
                result.add(new Tag(id,name));
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> setTag(Tag tag) {
        Result<Void> result = new Result<>();
        if (tag==null || tag.getName()==null){
            result.setSuccess(false);
            result.setMessage("内容不能为空");
            return result;
        }

        if (tag.getId()==null){
            baseMapper.insert(tag);
            log.debug("generated tagId : {}",tag.getId());
            setTagExistsInRedis(tag.getId(),tag.getName());
            return result;
        }

        String name = (String) tagMap.get(tag.getId().toString());
        if (tag.getName().equals(name)){
            return result;
        }

        setTagExistsInRedis(tag.getId(),tag.getName());
        baseMapper.updateById(tag);
        return result;
    }

    private void setTagExistsInRedis(Integer id, String name) {
        tagMap.put(String.valueOf(id),name);
    }

    private void setTagEmptyInRedis(Integer id){
        tagMap.put(String.valueOf(id),EMPTY_TAG);
    }
}
