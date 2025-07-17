package kr.hhplus.be.server.order.usecase.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka를 통해 주문 생성 이벤트를 전송하는 서비스
 */
@Service
public class OrderKafkaProducer {
  private final KafkaTemplate<String, String> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(OrderKafkaProducer.class);

  public OrderKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  /**
   * 주문 생성 이벤트를 Kafka로 전송하는 메서드
   * @param topic Kafka 토픽 이름
   * @param message 전송할 메시지
   */
  public void sendOrderMessage(String topic, String message) {
    kafkaTemplate.send(topic, message);
    logger.info("Kafka 주문 메시지 전송 - 토픽={}, 메시지={}", topic, message);
  }
}
