package kr.hhplus.be.server.balance;

import java.math.BigDecimal;

import kr.hhplus.be.server.user.User;

public interface BalanceReader {
  User findByIdForUpdate(Long userId);
  BigDecimal getBalance(Long userId);
  boolean hasEnoughBalance(Long userId, BigDecimal totalPrice);
  void deductBalance(Long userId, BigDecimal amount);
}
