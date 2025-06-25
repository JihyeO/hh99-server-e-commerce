package kr.hhplus.be.server.balance;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.user.User;

@Service
public class BalanceService {
  private final BalanceRepository balanceRepository;

  public BalanceService(BalanceRepository balanceRepository) {
    this.balanceRepository = balanceRepository;
  }

  public BigDecimal getBalance(Long userId) {
    User user = balanceRepository.findById(userId)
    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    return user.getBalance();
  }

  @Transactional
  public BigDecimal chargeBalance(Long userId, BigDecimal amount) {
    User user = balanceRepository.findByIdForUpdate(userId)
    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    user.chargeBalance(amount);
    
    return user.getBalance();
  }

  @Transactional
  public BigDecimal deductBalance(Long userId, BigDecimal amount) {
    User user = balanceRepository.findByIdForUpdate(userId)
    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    user.deductBalance(amount);

    return user.getBalance();
  }
}
