package kr.hhplus.be.server.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {
  
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false)
  private int quantity;

  protected Product() {}

  public Product(String name, BigDecimal price, int quantity) {
    this.name = name;
    this.price = price;
    this.quantity = quantity;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
