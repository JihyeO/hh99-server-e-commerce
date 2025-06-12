package kr.hhplus.be.server.order;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class OrderEntity {
  @Id
  private Long id;
  private Long userId;
  private Long userCouponId;
  private String status;
  private LocalDateTime orderDate;
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItemEntity> items;  

  public static OrderEntity fromDomain(Order order) {
    OrderEntity entity = new OrderEntity();
    entity.id = order.getId();
    entity.userId = order.getUserId();
    entity.userCouponId = order.getUserCouponId();
    entity.status = order.getStatus();
    entity.orderDate = order.getOrderDate();

    List<OrderItemEntity> itemEntities = order.getItems().stream()
      .map(OrderItemEntity::fromDomain)
      .toList();
    for (OrderItemEntity itemEntity : itemEntities) {
        itemEntity.setOrder(entity);
    }
    entity.items = itemEntities;
    return entity;
  }

  public Order toDomain() {
    List<OrderItem> domainItems = items.stream()
      .map(OrderItemEntity::toDomain)
      .toList();

    return new Order(userId, userCouponId, status, orderDate, domainItems);
  }

  public Long getId() {
    return this.id;
  }
}
