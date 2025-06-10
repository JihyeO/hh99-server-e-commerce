package kr.hhplus.be.server.user;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private BigDecimal balance;

  protected User() {}

  public User(String name, BigDecimal balance) {
    this.name = name;
    this.balance = balance;
  }

  public Long getId() {
        return id;
    }

  public String getName() {
      return name;
  }

  public BigDecimal getBalance() {
      return balance;
  }

  public void chargeBalance(BigDecimal price) {
    if (balance.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("충전금액은 0보다 커야 합니다.");
    }
    this.balance = this.balance.add(price);
  }

  public void deductBalance(BigDecimal price) {
    this.balance = this.balance.subtract(price);
  }
}
