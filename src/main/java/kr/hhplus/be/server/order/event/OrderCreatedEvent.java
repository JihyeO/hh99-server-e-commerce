package kr.hhplus.be.server.order.event;

public class OrderCreatedEvent {
  private final Long orderId;
  private final Long userId;
  private final String status;

  public OrderCreatedEvent(Long orderId, Long userId, String status) {
    this.orderId = orderId;
    this.userId = userId;
    this.status = status;
  }

  public Long getOrderId() {
    return orderId;
  }

  public Long getUserId() {
    return userId;
  }

  public String getStatus() {
    return status;
  }
}
