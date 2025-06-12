package kr.hhplus.be.server.balance;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.hhplus.be.server.user.User;

public interface BalanceRepository extends JpaRepository<User, Long> {
}
