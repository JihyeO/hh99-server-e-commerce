package kr.hhplus.be.server.order;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import kr.hhplus.be.server.product.Product;
import kr.hhplus.be.server.product.ProductRepository;
import kr.hhplus.be.server.user.User;
import kr.hhplus.be.server.user.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
public class OrderConcurrencyTest {
  private Long userId;
  private Long productId;

  @Container
  static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
      .withDatabaseName("hhplus")
      .withUsername("application")
      .withPassword("application");

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private OrderRepository orderRepository;

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mysql::getJdbcUrl);
    registry.add("spring.datasource.username", mysql::getUsername);
    registry.add("spring.datasource.password", mysql::getPassword);
  }
  
  @BeforeEach
  void setUp() {
    User mockUser = new User("Johb", new BigDecimal("100000"));
    userRepository.save(mockUser);
    Product mockProduct1 = new Product("Shirts", new BigDecimal("20000"), 100);
    Product mockProduct2 = new Product("Pants", new BigDecimal("25000"), 100);
    productRepository.save(mockProduct1);
    productRepository.save(mockProduct2);

    this.userId = mockUser.getId();
    this.productId = mockProduct1.getId();
  }

  public void requestOrder() throws Exception {
    String requestBody = """
    {
        "userId": %d,
        "userCouponId": null,
        "status": "ORDERED",
        "orderDate": "2024-06-12T15:00:00",
        "items": [
            {
                "productId": %d,
                "quantity": 2
            }
        ]
    }
    """.formatted(userId, productId);

    mockMvc.perform(post("/order")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists());
  }

  @Test
  public void orderAtTheSameTime() throws Exception {
    int threadCount = 2;
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch endLatch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      new Thread(() -> {
          try {
              startLatch.await();
              requestOrder();
          } catch (Exception e) {
              e.printStackTrace();
          } finally {
              endLatch.countDown();
          }
      }).start();
    }

    startLatch.countDown();
    endLatch.await();

    Assertions.assertEquals(2, orderRepository.getAllOrders().size());
  }
}
