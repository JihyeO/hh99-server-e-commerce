package kr.hhplus.be.server.order.usecase.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.hhplus.be.server.order.event.OrderCreatedEvent;

/**
 * Kafka를 통해 주문 생성 이벤트를 소비하는 서비스
 */
@Service
public class OrderKafkaConsumer {
  private final WebClient webClient;
  private final ObjectMapper objectMapper;
  private static final Logger logger = LoggerFactory.getLogger(OrderKafkaConsumer.class);

  public OrderKafkaConsumer(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
    this.webClient = webClientBuilder.baseUrl("http://localhost:3000").build();
    this.objectMapper = objectMapper;
  }

  /**
   * 주문 생성 이벤트를 처리하는 메서드
   * @param record Kafka로부터 수신한 메시지
   */
  @KafkaListener(topics = "order-created", groupId = "data-platform-service")
  public void consumeOrderCreatedEvent(ConsumerRecord<String, String> record) {
    logger.info("Kafka 주문 생성 이벤트 수신 - key={}, value={}", record.key(), record.value());

    try {
      OrderCreatedEvent orderEvent = objectMapper.readValue(record.value(), OrderCreatedEvent.class);

      // 데이터 플랫폼으로의 Mock API 호출
      webClient.post()
        .uri("/order/complete")
        .bodyValue(orderEvent)
        .retrieve()
        .bodyToMono(String.class)
        .doOnNext(response -> logger.info("Mock API 응답: {}", response))
        .doOnError(error -> logger.error("Mock API 호출 실패", error))
        .subscribe();

    } catch (Exception e) {
      logger.error("Kafka 메시지 파싱 실패", e);
    }
  }
}
