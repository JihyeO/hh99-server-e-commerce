package kr.hhplus.be.server.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.hhplus.be.server.order.usecase.PlaceOrderCommand;
import kr.hhplus.be.server.order.usecase.PlaceOrderInput;
import kr.hhplus.be.server.order.usecase.PlaceOrderResult;

@RestController
@RequestMapping("/order")
public class OrderController {
  private final PlaceOrderInput usecase;

  OrderController(PlaceOrderInput usecase) {
    this.usecase = usecase;
  }

  public static record OrderRequest(
    Long userId, 
    Long userCouponId, 
    String status,
    LocalDateTime orderDate, 
    List<OrderItem> items
  ) {}
  public static record OrderResponse(
    Long id
  ) {}

  @PostMapping
  public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest request) {
    PlaceOrderResult result = usecase.place(new PlaceOrderCommand(request.userId(), request.userCouponId(), request.status(), request.orderDate(), request.items()));
    return ResponseEntity.ok(new OrderResponse(result.id()));
  }
}