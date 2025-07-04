package kr.hhplus.be.server.usercoupon;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

/* 
 * 사용자 쿠폰 서비스
 * 
 * 사용자 쿠폰 발급 로직을 처리하는 서비스입니다.
 */
@Service
public class UserCouponService {
  private final UserCouponRepository userCouponRepository;

  public UserCouponService(UserCouponRepository userCouponRepository) {
    this.userCouponRepository = userCouponRepository;
  }

  /**
   * 사용자에게 쿠폰을 발급합니다.
   * 
   * @param userId 발급받는 사용자 ID
   * @param couponId 발급할 쿠폰 ID
   * @param validTo 쿠폰 유효 날짜
   */
  public void issueUserCoupon(Long userId, Long couponId, LocalDateTime validTo) {
    UserCoupon userCoupon = new UserCoupon();
    userCoupon.setUserId(userId);
    userCoupon.setCouponId(couponId);
    userCoupon.setStatus("ISSUED");
    userCoupon.setIssuedAt(LocalDateTime.now());
    userCoupon.setValidTo(validTo);
    userCouponRepository.save(userCoupon);
  }
}
