package kr.hhplus.be.server.order;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class OrderEntity {
  @Id
  private Long id;
  private Long userId;
  private Long userCouponId;
  private String status;
  private LocalDateTime orderDate;
  private List<OrderItem> items;

  public static OrderEntity fromDomain(Order order) {
      OrderEntity entity = new OrderEntity();
      entity.id = order.getId();
      entity.userId = order.getUserId();
      entity.userCouponId = order.getUserCouponId();
      entity.status = order.getStatus();
      entity.orderDate = order.getOrderDate();
      entity.items = order.getItems();
      return entity;
  }

  public Order toDomain() {
      return new Order(userId, userCouponId, status, orderDate, items);
  }

  public Long getId() {
    return this.id;
  }
}
