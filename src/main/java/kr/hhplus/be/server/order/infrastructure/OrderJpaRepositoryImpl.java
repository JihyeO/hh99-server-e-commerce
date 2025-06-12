package kr.hhplus.be.server.order.infrastructure;

import kr.hhplus.be.server.order.Order;
import kr.hhplus.be.server.order.OrderEntity;
import kr.hhplus.be.server.order.OrderRepository;

public class OrderJpaRepositoryImpl implements OrderRepository {
  private final OrderJpaRepository jpaRepo;

  OrderJpaRepositoryImpl(OrderJpaRepository jpaRepo) {
    this.jpaRepo = jpaRepo;
  }

  @Override
  public Order save(Order order) {
    OrderEntity entity = OrderEntity.fromDomain(order);
    OrderEntity savedEntity = jpaRepo.save(entity);
    Order savedOrder = savedEntity.toDomain();
    order.setId(savedOrder.getId());
    return savedOrder;
  }
} 
