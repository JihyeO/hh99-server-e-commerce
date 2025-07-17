package kr.hhplus.be.server.order.usecase.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka를 통해 주문 생성 이벤트를 소비하는 서비스
 */
@Service
public class OrderKafkaConsumer {
  private static final Logger logger = LoggerFactory.getLogger(OrderKafkaConsumer.class);

  /**
   * 주문 생성 이벤트를 처리하는 메서드
   * @param message Kafka로부터 수신한 메시지
   */
  @KafkaListener(topics = "order-created", groupId = "data-platform-service")
  public void consumeOrderCreatedEvent(ConsumerRecord<String, String> record) {
    logger.info("Kafka 주문 생성 이벤트 수신 - key={}, value={}", record.key(), record.value());
  }
}
