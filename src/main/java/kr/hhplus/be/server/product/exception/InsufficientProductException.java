package kr.hhplus.be.server.product.exception;

public class InsufficientProductException extends RuntimeException {
  public InsufficientProductException(String message) {
    super(message);
  }

}
