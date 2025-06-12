package kr.hhplus.be.server.order.usecase;

public interface PlaceOrderInput {
  PlaceOrderResult place(PlaceOrderCommand command);
}
