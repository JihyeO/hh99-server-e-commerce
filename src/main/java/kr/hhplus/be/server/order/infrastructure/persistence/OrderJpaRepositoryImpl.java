package kr.hhplus.be.server.order.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import kr.hhplus.be.server.order.Order;
import kr.hhplus.be.server.order.OrderEntity;
import kr.hhplus.be.server.order.OrderRepository;

@Repository
public class OrderJpaRepositoryImpl implements OrderRepository {
  private final SpringOrderJpaRepository jpaRepo;

  public OrderJpaRepositoryImpl(SpringOrderJpaRepository jpaRepo) {
    this.jpaRepo = jpaRepo;
  }

  @Override
  public List<Order> getAllOrders() {
    List<Order> orders = new ArrayList<>();
    for (OrderEntity orderEntity : jpaRepo.findAll()) {
      orders.add(orderEntity.toDomain());
    }
    return orders;
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
