package kr.hhplus.be.server.order;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
  private Long id;
  private final Long userId;
  private final Long userCouponId;
  private final String status;
  private final LocalDateTime orderDate;
  private final List<OrderItem> items;

  public Order(Long userId, Long userCouponId, String status, LocalDateTime orderDate, List<OrderItem> items) {
    this.userId = userId;
    this.userCouponId = userCouponId;
    this.status = status;
    this.orderDate = orderDate;
    this.items = items;
  }

  public static Order create(Long userId, Long userCouponId, String status, LocalDateTime orderDate, List<OrderItem> items) {
    return new Order(userId, userCouponId, status, orderDate, items);
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public Long getUserId() {
    return userId;
  }

  public Long getUserCouponId() {
    return userCouponId;
  }

  public String getStatus() {
    return status;
  }

  public LocalDateTime getOrderDate() {
    return orderDate;
  }

  public List<OrderItem> getItems() {
    return items;
  }
}
