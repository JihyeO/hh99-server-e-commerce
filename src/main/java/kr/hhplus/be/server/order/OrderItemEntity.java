package kr.hhplus.be.server.order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public Long getProductId() {
      return productId;
    }

    public void setProductId(Long productId) {
      this.productId = productId;
    }

    public int getQuantity() {
      return quantity;
    }

    public void setQuantity(int quantity) {
      this.quantity = quantity;
    }

    public OrderEntity getOrder() {
      return order;
    }

    public void setOrder(OrderEntity order) {
      this.order = order;
    }

    public static OrderItemEntity fromDomain(OrderItem item) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.productId = item.getProductId();
        entity.quantity = item.getQuantity();
        return entity;
    }

    public OrderItem toDomain() {
        return new OrderItem(order.getId(), productId, quantity);
    }
}
