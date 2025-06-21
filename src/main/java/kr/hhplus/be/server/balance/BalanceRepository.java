package kr.hhplus.be.server.balance;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.user.User;

public interface BalanceRepository extends JpaRepository<User, Long> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT u FROM User u WHERE u.id = :userId")
  Optional<User> findByIdForUpdate(@Param("userId") Long userId);
}