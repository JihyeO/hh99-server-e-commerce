package kr.hhplus.be.server.balance;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import kr.hhplus.be.server.user.User;
import kr.hhplus.be.server.user.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class BalanceConcurrencyTest {
  private Long userId;

  @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
        .withDatabaseName("hhplus")
        .withUsername("application")
        .withPassword("application");

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BalanceService balanceService;

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
      registry.add("spring.datasource.url", mysql::getJdbcUrl);
      registry.add("spring.datasource.username", mysql::getUsername);
      registry.add("spring.datasource.password", mysql::getPassword);
      registry.add("spring.datasource.driver-class-name", mysql::getDriverClassName);
  }

  @BeforeEach
    void setUp() {
        User mockUser = new User("Johb", new BigDecimal("100000"));
        userRepository.save(mockUser);
        this.userId = mockUser.getId();
    }

  @Test
  public void chargeBalanceAtTheSameTime() throws InterruptedException {
    int threadCount = 2;
    BigDecimal chargeAmount = new BigDecimal("10000");
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch endLatch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
        new Thread(() -> {
            try {
                startLatch.await();
                balanceService.chargeBalance(userId, chargeAmount);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                endLatch.countDown();
            }
        }).start();
    }

    startLatch.countDown();
    endLatch.await();

    User user = userRepository.findById(userId).orElseThrow();
    Assertions.assertEquals(0, user.getBalance().compareTo(new BigDecimal("120000"))); // 100000 + (10000 * 2)
  }

  @Test
  public void deductBalanceAtTheSameTime() throws InterruptedException {
    int threadCount = 2;
    BigDecimal chargeAmount = new BigDecimal("10000");
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch endLatch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
        new Thread(() -> {
            try {
                startLatch.await();
                balanceService.deductBalance(userId, chargeAmount);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                endLatch.countDown();
            }
        }).start();
    }

    startLatch.countDown();
    endLatch.await();

    User user = userRepository.findById(userId).orElseThrow();
    Assertions.assertEquals(0, user.getBalance().compareTo(new BigDecimal("80000"))); // 100000 - (10000 * 2)
  }
}
