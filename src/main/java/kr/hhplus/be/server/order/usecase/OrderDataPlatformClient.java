package kr.hhplus.be.server.order.usecase;

import kr.hhplus.be.server.order.event.OrderCreatedEvent;

/* 
 * 데이터 플랫폼을 위한 주문 데이터 전송 클라이언트 인터페이스
 */
public interface OrderDataPlatformClient {
  void sendOrderData(OrderCreatedEvent event);
}
