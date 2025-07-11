package kr.hhplus.be.server.order.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.hhplus.be.server.order.OrderEntity;

public interface SpringOrderJpaRepository extends JpaRepository<OrderEntity, Long> {
}
