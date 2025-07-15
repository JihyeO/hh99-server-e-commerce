package kr.hhplus.be.server.order.infrastructure.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.hhplus.be.server.order.event.OrderCreatedEvent;
import kr.hhplus.be.server.order.usecase.OrderDataPlatformClient;

/**
 * Mock API를 호출하여 주문 데이터를 전송하는 클라이언트 구현
 * 실제 API 호출 대신 로그로 대체
 */
@Component
public class MockOrderDataPlatformClient implements OrderDataPlatformClient {
  private static final Logger logger = LoggerFactory.getLogger(MockOrderDataPlatformClient.class);
    @Override
    public void sendOrderData(OrderCreatedEvent event) {
        logger.info("Mock API 호출: 주문 데이터 전송 - 주문 Id={}, 사용자 Id={}, 주문 상태={}",
         event.getOrderId(), event.getUserId(), event.getStatus());
    }
}
