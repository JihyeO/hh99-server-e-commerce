package kr.hhplus.be.server.order.usecase;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kr.hhplus.be.server.balance.BalanceReader;
import kr.hhplus.be.server.balance.exception.InsufficientBalanceException;
import kr.hhplus.be.server.order.Order;
import kr.hhplus.be.server.order.OrderItem;
import kr.hhplus.be.server.order.OrderRepository;
import kr.hhplus.be.server.product.Product;
import kr.hhplus.be.server.product.ProductReader;
import kr.hhplus.be.server.product.exception.InsufficientProductException;

@Service
public class PlaceOrderInteractor implements PlaceOrderInput {
  private final OrderRepository orderRepository;
  private final BalanceReader balanceReader;
  private final ProductReader productReader;

  public PlaceOrderInteractor(
    OrderRepository orderRepository, 
    BalanceReader balanceReader, 
    ProductReader productReader
    ) {
    this.orderRepository = orderRepository;
    this.balanceReader = balanceReader;
    this.productReader = productReader;
  }

  @Override
  public PlaceOrderResult place(PlaceOrderCommand c) {
    Order order = Order.create(c.userId(), c.userCouponId(), c.status(), c.orderDate(), c.items());
    
    // 재고 차감
    for (OrderItem orderItem : c.items()) {
      if (!productReader.hasEnoughProduct(orderItem.getProductId(), orderItem.getQuantity())) {
        throw new InsufficientProductException("재고가 부족합니다.");
      } else {
        productReader.deductProduct(orderItem.getProductId(), orderItem.getQuantity());
      }
    }
    
    // 총 금액 계산 및 잔액 차감
    // todo: 쿠폰 적용
    Map<Long, Product> productMap = productReader.findAllById(
      order.getItems().stream().map(OrderItem::getProductId).toList()
    ).stream().collect(Collectors.toMap(Product::getId, Function.identity()));
    BigDecimal totalPrice = order.calculateTotalAmount(productMap);
    if (!balanceReader.hasEnoughBalance(c.userId(), totalPrice)) {
      throw new InsufficientBalanceException("잔액이 부족합니다.");
    } else {
      balanceReader.deductBalance(c.userId(), totalPrice);
    }
  
    Order saved = orderRepository.save(order);

    return new PlaceOrderResult(saved.getId());
  }
}