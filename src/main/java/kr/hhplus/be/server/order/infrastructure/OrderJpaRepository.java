package kr.hhplus.be.server.order.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.hhplus.be.server.order.OrderEntity;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
}
