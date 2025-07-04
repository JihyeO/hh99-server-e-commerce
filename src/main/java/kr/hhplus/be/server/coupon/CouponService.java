package kr.hhplus.be.server.coupon;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.usercoupon.UserCouponService;

/**
 * 쿠폰 서비스
 * 
 * 쿠폰 발급 로직을 처리하는 서비스입니다.
 */
@Service
public class CouponService {
  private final CouponRepository couponRepository;
  private final UserCouponService userCouponService;

  public CouponService(CouponRepository couponRepository, UserCouponService userCouponService) {
    this.couponRepository = couponRepository;
    this.userCouponService = userCouponService;
  }
  
  /**
   * 쿠폰을 발급합니다.
   * 
   * @param userId 발급받는 사용자 ID
   * @param couponId 발급할 쿠폰 ID
   * @throws IllegalArgumentException 쿠폰이 존재하지 않을 경우
   * @throws IllegalStateException 쿠폰이 이미 발급 마감된 경우
   */
  @Transactional
  public void issueCoupon(Long userId, Long couponId) {
    Coupon coupon = couponRepository.findByIdForUpdate(couponId)
    .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 쿠폰입니다."));

    if (coupon.getCurrentQuantity() <= 0) {
      throw new IllegalStateException("발급 마감된 쿠폰입니다.");
    }

    coupon.issue();
    couponRepository.save(coupon);

    userCouponService.issueUserCoupon(
      userId, 
      couponId, 
      coupon.getValidTo()
    );
  }
}
