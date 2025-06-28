package kr.hhplus.be.server.order;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import kr.hhplus.be.server.common.infrastructure.RedisLockService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration test for RedisLockService using Testcontainers.
 *
 * Spins up a Redis container and verifies distributed lock behavior:
 * - successful lock acquisition and release
 * - failure to acquire lock when already held (duplicate lock)
 */
@Testcontainers
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RedisLockServiceIntegrationTest {

  // Redis container for testing
  @Container
  static GenericContainer<?> redis = new GenericContainer<>("redis:7")
    .withExposedPorts(6379);

  /**
   * Overrides Spring Redis properties to connect to the Testcontainers Redis.
   */  
  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.redis.host", redis::getHost);
    registry.add("spring.redis.port", () -> redis.getMappedPort(6379));
  }

  @Autowired
  RedisLockService redisLockService;

  /**
   * Tests that a lock can be acquired and the task returns the expected result.
   */
  @Test
  void LockAndRelease() {
    String result = redisLockService.runWithLock("test:lock", Duration.ofSeconds(2), () -> "done");
    assertEquals("done", result);
  }

  /**
   * Tests that a second lock attempt fails when the first lock is held.
   */
  @Test
  void DuplicateLock() throws InterruptedException {
    String key = "test:lock";
    AtomicReference<String> secondResult = new AtomicReference<>();

    Thread t1 = new Thread(() ->
        redisLockService.runWithLock(key, Duration.ofSeconds(2), () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
            return "first";
        }));

    Thread t2 = new Thread(() -> {
        try {
            redisLockService.runWithLock(key, Duration.ofMillis(500), () -> "second");
        } catch (Exception e) {
            secondResult.set(e.getMessage());
        }
    });

    t1.start();
    Thread.sleep(100); // ensure t1 locks first
    t2.start();

    t1.join(); 
    t2.join();

    assertTrue(secondResult.get().contains("Lock acquisition failed"));
  }
}