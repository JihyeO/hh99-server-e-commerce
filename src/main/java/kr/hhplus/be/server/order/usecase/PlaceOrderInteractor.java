package kr.hhplus.be.server.order.usecase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.balance.BalanceReader;
import kr.hhplus.be.server.balance.exception.InsufficientBalanceException;
import kr.hhplus.be.server.order.Order;
import kr.hhplus.be.server.order.OrderItem;
import kr.hhplus.be.server.order.OrderRepository;
import kr.hhplus.be.server.order.event.OrderCreatedEvent;
import kr.hhplus.be.server.product.Product;
import kr.hhplus.be.server.product.ProductReader;
import kr.hhplus.be.server.product.exception.InsufficientProductException;
import kr.hhplus.be.server.user.User;

@Service
@Transactional
public class PlaceOrderInteractor implements PlaceOrderInput {
  private final OrderRepository orderRepository;
  private final BalanceReader balanceReader;
  private final ProductReader productReader;
  private final ApplicationEventPublisher eventPublisher;

  public PlaceOrderInteractor(
    OrderRepository orderRepository, 
    BalanceReader balanceReader, 
    ProductReader productReader,
    ApplicationEventPublisher eventPublisher
    ) {
    this.orderRepository = orderRepository;
    this.balanceReader = balanceReader;
    this.productReader = productReader;
    this.eventPublisher = eventPublisher;
  }

  @Override
  public PlaceOrderResult place(PlaceOrderCommand c) {
    Order order = Order.create(c.userId(), c.userCouponId(), c.status(), c.orderDate(), c.items());
    List<Long> productIds = order.getItems().stream()
      .map(OrderItem::getProductId)
      .toList();

    // 총 금액 계산
    Map<Long, Product> lockedProductMap = productReader
      .findAllByIdForUpdate(productIds).stream()
      .collect(Collectors.toMap(Product::getId, Function.identity()));
    BigDecimal totalPrice = order.calculateTotalAmount(lockedProductMap);

    // 잔액 차감
    // todo: 쿠폰 적용
    User user = balanceReader.findByIdForUpdate(c.userId());
    if (user.getBalance().compareTo(totalPrice) < 0) {
      throw new InsufficientBalanceException("잔액이 부족합니다.");
    }
    balanceReader.deductBalance(user.getId(), totalPrice);
    
    // 재고 차감
    for (OrderItem orderItem : c.items()) {
      Product product = lockedProductMap.get(orderItem.getProductId());
      if (product == null) {
            throw new IllegalStateException("상품이 존재하지 않습니다: " + orderItem.getProductId());
        }
      if (product.getQuantity() < orderItem.getQuantity()) {
        throw new InsufficientProductException("재고가 부족합니다.");
      }
      productReader.deductProduct(product.getId(), orderItem.getQuantity());
    }
    
    // 주문 저장
    Order saved = orderRepository.save(order);

    // 주문 생성 이벤트 발행
    eventPublisher.publishEvent(new OrderCreatedEvent(saved.getId(), saved.getUserId(), saved.getStatus()));
    return new PlaceOrderResult(saved.getId());
  }
}