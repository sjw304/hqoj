package top.quezr.hqoj.util.event;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executors;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/12 10:31
 */
public class CenterEventBus {
    public static final AsyncEventBus bus = new AsyncEventBus(Executors.newFixedThreadPool(4));
}
