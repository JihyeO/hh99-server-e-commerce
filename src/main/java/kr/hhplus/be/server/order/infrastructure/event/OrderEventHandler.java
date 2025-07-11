package kr.hhplus.be.server.order.infrastructure.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import kr.hhplus.be.server.order.event.OrderCreatedEvent;
import kr.hhplus.be.server.order.usecase.OrderDataPlatformClient;

/**
 * 주문 생성 이벤트를 처리하여 데이터 플랫폼에 주문 데이터를 전송하는 핸들러
 */
@Component
public class OrderEventHandler {
  private final OrderDataPlatformClient orderDataPlatformClient;

  public OrderEventHandler(OrderDataPlatformClient orderDataPlatformClient) {
    this.orderDataPlatformClient = orderDataPlatformClient;
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleOrderCreatedEvent(OrderCreatedEvent event) {
    orderDataPlatformClient.sendOrderData(event);
  }
}
