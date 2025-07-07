package kr.hhplus.be.server.coupon;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT c FROM Coupon c WHERE c.id = :id")
  Optional<Coupon> findByIdForUpdate(@Param("id") Long id);
}
