package kr.hhplus.be.server.balance;

import java.math.BigDecimal;

public interface BalanceReader {
  BigDecimal getBalance(Long userId);
  boolean hasEnoughBalance(Long userId, BigDecimal totalPrice);
  void deductBalance(Long userId, BigDecimal amount);
}
