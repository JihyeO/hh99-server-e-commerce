package kr.hhplus.be.server.order;

import java.util.List;

public interface OrderRepository {
  List<Order> getAllOrders();
  Order save(Order order);
}
