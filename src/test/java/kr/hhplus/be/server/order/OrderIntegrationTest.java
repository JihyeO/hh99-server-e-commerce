package kr.hhplus.be.server.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
public class OrderIntegrationTest {
    private Long userId;
    private Long productId1;

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
        registry.add("spring.datasource.driver-class-name", mysql::getDriverClassName);
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
        this.productId1 = mockProduct1.getId();
    }

    @Test
    public void orderSuccessTest() throws Exception {
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
        """.formatted(userId, productId1);

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists());

        Assertions.assertEquals(1, orderRepository.getAllOrders().size());
    }
}
