package kr.hhplus.be.server.order.usecase;

import java.time.Duration;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import kr.hhplus.be.server.common.infrastructure.RedisLockService;

/**
 * PlaceOrderInput implementation that applies Redis distributed locking.
 * 
 * Delegates actual order placement to the wrapped PlaceOrderInput,
 * but ensures concurrency control by acquiring a Redis lock per user.
 */
@Primary
@Service
public class DistributedLockingPlaceOrderInteractor implements PlaceOrderInput {

    private final PlaceOrderInput delegate;
    private final RedisLockService redisLockService;

    /**
     * Constructor injecting the delegate and RedisLockService.
     *
     * @param delegate the actual PlaceOrderInput implementation to delegate to
     * @param redisLockService the Redis-based distributed lock service
     */
    public DistributedLockingPlaceOrderInteractor(
        PlaceOrderInput delegate,
        RedisLockService redisLockService
    ) {
        this.delegate = delegate;
        this.redisLockService = redisLockService;
    }

    /**
     * Places an order while holding a distributed lock to prevent concurrent modifications
     * for the same user.
     *
     * @param command the order command containing order details and user ID
     * @return the result of placing the order
     */
    @Override
    public PlaceOrderResult place(PlaceOrderCommand command) {
        String key = "lock:order:user:" + command.userId();

        return redisLockService.runWithLock(key, Duration.ofSeconds(3), () -> {
            return delegate.place(command);
        });
    }
}