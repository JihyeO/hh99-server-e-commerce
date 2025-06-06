package kr.hhplus.be.server.usecase;

import java.time.LocalDateTime;
import java.util.List;

import kr.hhplus.be.server.order.OrderItem;

public record PlaceOrderCommand(
  Long userId, 
  Long userCouponId,
  String status,
  LocalDateTime orderDate, 
  List<OrderItem> items
) {}
