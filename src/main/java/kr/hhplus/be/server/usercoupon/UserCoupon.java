package kr.hhplus.be.server.usercoupon;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_coupons")
@Getter
@Setter
public class UserCoupon {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private Long userId;
  @Column(nullable = false)
  private Long couponId;
  @Column(nullable = false, length = 10)
  private String status; // "ISSUED", "USED", "EXPIRED"
  @Column(nullable = false)
  private LocalDateTime issuedAt;
  @Column(nullable = false)
  private LocalDateTime validTo;
  @Column(nullable = true)
  private LocalDateTime usedAt;
}
