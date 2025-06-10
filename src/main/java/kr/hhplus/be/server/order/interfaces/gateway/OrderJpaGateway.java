package kr.hhplus.be.server.order.interfaces.gateway;

import kr.hhplus.be.server.order.Order;
import kr.hhplus.be.server.order.OrderEntity;
import kr.hhplus.be.server.order.OrderRepository;

public class OrderJpaGateway implements OrderRepository {
  private final SpringDataOrderRepo jpaRepo;

  OrderJpaGateway(SpringDataOrderRepo jpaRepo) {
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
