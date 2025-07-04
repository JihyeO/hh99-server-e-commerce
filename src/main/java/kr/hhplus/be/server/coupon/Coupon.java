package kr.hhplus.be.server.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "coupons")
@Getter
public class Coupon {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String code;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private BigDecimal discountAmount;
  @Column(nullable = false)
  private int totalQuantity;
  @Column(nullable = false)
  private int issuedQuantity;
  @Column(nullable = false)
  private LocalDateTime validFrom;
  @Column(nullable = false)
  private LocalDateTime validTo;

  protected Coupon() {}

  public Coupon(String code, String name, BigDecimal discountAmount, int totalQuantity, LocalDateTime validFrom, LocalDateTime validTo) {
    this.code = code;
    this.name = name;
    this.discountAmount = discountAmount;
    this.totalQuantity = totalQuantity;
    this.issuedQuantity = 0;
    this.validFrom = validFrom;
    this.validTo = validTo;
  }

  public int getCurrentQuantity() {
    return this.totalQuantity - issuedQuantity;
  }

  public void issue() {
    this.issuedQuantity += 1;
  }
}
