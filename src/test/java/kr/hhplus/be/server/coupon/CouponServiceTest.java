package kr.hhplus.be.server.coupon;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import kr.hhplus.be.server.usercoupon.UserCouponService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 쿠폰 서비스 테스트
 * 
 * 쿠폰 발급 로직을 검증하는 단위 테스트입니다.
 */
@RunWith(MockitoJUnitRunner.class)
public class CouponServiceTest {
  @Mock
  private CouponRepository couponRepository;

  @Mock
  private UserCouponService userCouponService;

  @InjectMocks
  private CouponService couponService;

  /**
   * 테스트용 쿠폰 객체 생성
   * 
   * @return Coupon 객체
   */
  private Coupon createCoupon() throws Exception {
    Coupon mockCoupon = new Coupon(
      "COUPON-TEST1",
      "Test Coupon 1",
      new BigDecimal("1000"),
      10,
      LocalDateTime.now(),
      LocalDateTime.now().plusDays(30)
    );
    Field idField = Coupon.class.getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(mockCoupon, 1L);
    return mockCoupon;
  }

  /**
   * 쿠폰 발급 테스트
   */
  @Test
  public void testIssueCoupon() throws Exception {
    // Given
    Coupon mockCoupon = createCoupon();

    // When
    when(couponRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(mockCoupon));

    // Then
    couponService.issueCoupon(1L, 1L);
    assertThat(mockCoupon.getIssuedQuantity()).isEqualTo(1);
    verify(userCouponService).issueUserCoupon(
      eq(1L),   // userId
      eq(1L),   // couponId
      eq(mockCoupon.getValidTo())   // 유효기간
  );
  }

  /**
   * 발급 가능한 쿠폰이 존재하지 않을 경우 예외 발생 테스트
   */
  @Test
  public void testIssueCoupon_ExceedingLimit() throws Exception {
    // Given
    Coupon mockCoupon = createCoupon();
    mockCoupon.issue();

    // When
    when(couponRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(mockCoupon));

    // Then
    try {
      couponService.issueCoupon(1L, 1L);
    } catch (IllegalStateException e) {
      assertThat(e.getMessage()).isEqualTo("쿠폰 발급 한도를 초과했습니다.");
    }
  }

}
