package kr.hhplus.be.server.order;

import java.math.BigDecimal;

import kr.hhplus.be.server.product.Product;

public class OrderItem {
  private Long id;
  private final Long orderId;
  private final Long productId;
  private final int quantity;

  private OrderItem(Long orderId, Long productId, int quantity) {
    this.orderId = orderId;
    this.productId = productId;
    this.quantity = quantity;
  }

  public static OrderItem create(Long id, Long orderId, Long productId, int quantity) {
    return new OrderItem(orderId, productId, quantity);
  }

  public Long getId() {
    return id;
  }

  public Long getOrderId() {
    return orderId;
  }

  public Long getProductId() {
    return productId;
  }

  public int getQuantity() {
    return quantity;
  }

  public BigDecimal getTotalPrice(Product product) {
    return product.getPrice().multiply(BigDecimal.valueOf(quantity));
  }
}
