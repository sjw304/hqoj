package top.quezr.hqoj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import top.quezr.hqoj.entity.LikeEvent;
import top.quezr.hqoj.enums.ItemType;
import top.quezr.hqoj.enums.LikeType;
import top.quezr.hqoj.mapper.LikeEventMapper;
import top.quezr.hqoj.service.LikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.quezr.hqoj.support.Result;
import top.quezr.hqoj.util.event.CenterEventBus;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author que
 * @since 2021-05-18
 */
@Service
public class LikeServiceImpl extends ServiceImpl<LikeEventMapper, LikeEvent> implements LikeService {

    private static String USER_LIKE_PROBLEM_SET_KEY = "like:%s:p:";
    private static String USER_LIKE_SOLUTION_SET_KEY = "like:%s:s:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private ExecutorService executorService = new ThreadPoolExecutor(1, 4, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(30), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"Save Like Log Thread");
        }
    });

    @PostConstruct
    private void init() {
        CenterEventBus.bus.register(this);
    }

    @Override
    public Result<Void> saveLikeEvent(LikeEvent event) {
        Result<Void> result = new Result<>();
        CenterEventBus.bus.post(event);

        // 将点赞信息放到redis里
        String key = String.format(event.getItemType() == ItemType.PROBLEM ? USER_LIKE_PROBLEM_SET_KEY : USER_LIKE_SOLUTION_SET_KEY, event.getUserId());
        if (event.getType() == LikeType.LIKE) {
            redisTemplate.boundSetOps(key).add(String.valueOf(event.getItemId()));
        }else {
            redisTemplate.boundSetOps(key).remove(String.valueOf(event.getItemId()));
        }

        // 异步
        saveEvent(event);
        return result;
    }

    @Override
    public Result<Boolean> isLiked(Integer userId, ItemType itemType, Integer itemId) {
        Result<Boolean> result = new Result<>();
        String key = String.format(itemType == ItemType.PROBLEM ? USER_LIKE_PROBLEM_SET_KEY : USER_LIKE_SOLUTION_SET_KEY,userId);
        Boolean member = redisTemplate.boundSetOps(key).isMember(String.valueOf(itemId));
        result.setData(!Objects.isNull(member) && member);
        return result;
    }

    /**
     * 使用线程池 进行异步同步到数据库
     * @param event
     */
    private void saveEvent(LikeEvent event){
        this.executorService.execute(()->baseMapper.insert(event));
    }
}
