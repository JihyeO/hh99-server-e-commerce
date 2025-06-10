package kr.hhplus.be.server.order.interfaces.gateway;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.hhplus.be.server.order.OrderEntity;

public interface SpringDataOrderRepo extends JpaRepository<OrderEntity, Long> {
}
