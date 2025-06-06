package kr.hhplus.be.server.user;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public BigDecimal getUserBalance(Long userId) {
    User user = userRepository.findById(userId)
    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    return user.getBalance();
  }

  @Transactional
  public BigDecimal chargeBalance(Long userId, BigDecimal price) {
    User user = userRepository.findById(userId)
    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    user.chargeBalance(price);
    
    return user.getBalance();
  }
}
