package kr.hhplus.be.server.usecase;

import kr.hhplus.be.server.order.Order;
import kr.hhplus.be.server.order.OrderRepository;

public class PlaceOrderInteractor implements PlaceOrderInput {
  private final OrderRepository repository;
  private final PlaceOrderOutput presenter;

  public PlaceOrderInteractor(OrderRepository repository, PlaceOrderOutput presenter) {
    this.repository = repository;
    this.presenter = presenter;
  }

  @Override
  public void place(PlaceOrderCommand c) {
    Order order = Order.create(c.userId(), c.userCouponId(), c.status(), c.orderDate(), c.items());
    Order saved = repository.save(order);
    presenter.ok(new PlaceOrderResult(saved.getId()));
  }
}