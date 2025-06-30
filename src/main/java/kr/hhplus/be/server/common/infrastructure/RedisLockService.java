package kr.hhplus.be.server.common.infrastructure;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * Provides distributed locking functionality using Redis.
 * 
 * Executes the given task if the lock for the specified key can be acquired within the timeout.
 */
public interface RedisLockService {

    /**
     * Executes the provided task within a distributed lock.
     *
     * @param key the lock key
     * @param timeout maximum time to try acquiring the lock
     * @param task the task to execute if lock is acquired
     * @param <T> the return type of the task
     * @return the result of the task execution
     * @throws IllegalStateException if the lock could not be acquired in time
     */
    <T> T runWithLock(String key, Duration timeout, Supplier<T> task);
}
