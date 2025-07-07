package kr.hhplus.be.server.order.infrastructure.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import kr.hhplus.be.server.order.event.OrderCreatedEvent;

@Component
public class OrderEventHandler {

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleOrderCreatedEvent(OrderCreatedEvent event) {
    // 주문 생성 이벤트 처리 로직
    // call mock API : todo
  }
}
