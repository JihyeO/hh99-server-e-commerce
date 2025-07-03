package kr.hhplus.be.server.order;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import kr.hhplus.be.server.order.usecase.ProductOrderRankingService;

/**
 * 이 테스트는 Redis를 사용하여 제품 주문 순위를 매기는 서비스의 기능을 검증합니다.
 */
@Testcontainers
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductOrderRankingServiceTest {

  @Container
  static GenericContainer<?> redis = new GenericContainer<>("redis:7")
    .withExposedPorts(6379);

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.redis.host", redis::getHost);
    registry.add("spring.redis.port", () -> redis.getMappedPort(6379));
  }

  @Autowired
  private ProductOrderRankingService productOrderRankingService;

  /**
   * 제품 주문 순위 기능을 테스트합니다.
   * - 제품 ID를 사용하여 점수를 증가시키고
   * - 오늘의 상위 3개 제품을 조회하여 올바른 순위가 매겨졌는지 검증합니다.
   */
  @Test
  public void testIncreaseProductScore() {
    // Given
    String productId1 = "1";
    String productId2 = "2";
    String productId3 = "3";

    // When
    productOrderRankingService.increaseProductScore(productId1);
    productOrderRankingService.increaseProductScore(productId2);
    productOrderRankingService.increaseProductScore(productId2);
    productOrderRankingService.increaseProductScore(productId2);
    productOrderRankingService.increaseProductScore(productId3);

    // Then
    String todayKey = productOrderRankingService.getTodayKey();
    Set<ZSetOperations.TypedTuple<String>> top3ProductsToday = productOrderRankingService.getTopProducts(todayKey, 3);
    System.out.println("=== Top 3 Products Today ===");
    top3ProductsToday.forEach(tuple -> {
      System.out.println("Product ID: " + tuple.getValue() + ", Score: " + tuple.getScore());
    });

    // Assertions 
    List<String> expectedTopProducts = List.of(productId2, productId3, productId1);
    List<String> actualTopProducts = top3ProductsToday.stream()
        .map(ZSetOperations.TypedTuple::getValue)
        .toList();
    Assertions.assertEquals(expectedTopProducts, actualTopProducts);
  }

}
