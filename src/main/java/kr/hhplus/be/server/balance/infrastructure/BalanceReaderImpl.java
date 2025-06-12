package kr.hhplus.be.server.balance.infrastructure;

import java.math.BigDecimal;

import kr.hhplus.be.server.balance.BalanceReader;
import kr.hhplus.be.server.balance.BalanceRepository;
import kr.hhplus.be.server.user.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BalanceReaderImpl implements BalanceReader {
  private final BalanceRepository balanceRepository;

  @Override
  public BigDecimal getBalance(Long userId) {
    User user = balanceRepository.findById(userId)
    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    return user.getBalance();
  }

  @Override
  public boolean hasEnoughBalance(Long userId, BigDecimal totalPrice) {
    return getBalance(userId).compareTo(totalPrice) < 0;
  }

  @Override
  public void deductBalance(Long userId, BigDecimal amount) {
    User user = balanceRepository.findById(userId)
    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    user.deductBalance(amount);
    balanceRepository.save(user);
  }
}
