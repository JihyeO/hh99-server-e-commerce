package kr.hhplus.be.server.common.infrastructure;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

/**
 * Redis-based implementation of distributed lock service.
 *
 * Uses Redis SET NX with expiration to acquire locks,
 * and a Lua script to safely release locks only if owned by the caller.
 */
@Service
public class RedisLockServiceImpl implements RedisLockService {

    // Lua script to release lock only if the lock value matches
    private static final String UNLOCK_LUA_SCRIPT =
        "if redis.call('get', KEYS[1]) == ARGV[1] then " +
        "   return redis.call('del', KEYS[1]) " +
        "else " +
        "   return 0 " +
        "end";

    private final StringRedisTemplate redisTemplate;

    /**
     * Constructor injecting Redis template for Redis operations.
     *
     * @param redisTemplate the StringRedisTemplate used to interact with Redis
     */
    public RedisLockServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Tries to acquire a distributed lock on the given key within the specified timeout,
     * executes the given task if the lock is acquired, and releases the lock afterward.
     *
     * @param key the lock key
     * @param timeout the lock expiration duration
     * @param task the critical section to execute under lock protection
     * @param <T> return type of the task
     * @return the result from the task execution
     * @throws IllegalStateException if unable to acquire the lock
     */
    @Override
    public <T> T runWithLock(String key, Duration timeout, Supplier<T> task) {
        String lockValue = UUID.randomUUID().toString();
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(
            key, lockValue, timeout
        );

        if (Boolean.FALSE.equals(acquired)) {
            throw new IllegalStateException("Lock acquisition failed for key: " + key);
        }

        try {
            return task.get();
        } finally {
            releaseLock(key, lockValue);
        }
    }

    /**
     * Releases the distributed lock only if the lock is currently held by the caller.
     *
     * @param key the lock key
     * @param value the unique lock value used to verify ownership
     */
    private void releaseLock(String key, String value) {
        RedisScript<Long> script = RedisScript.of(UNLOCK_LUA_SCRIPT, Long.class);
        redisTemplate.execute(script, Collections.singletonList(key), value);
    }
}

