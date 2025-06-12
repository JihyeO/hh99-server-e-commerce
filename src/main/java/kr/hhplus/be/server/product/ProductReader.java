package kr.hhplus.be.server.product;

import java.util.List;

public interface ProductReader {
  List<Product> findAllById(List<Long> ids);
  boolean hasEnoughProduct(Long id, int orderQuantity);
  void deductProduct(Long productId, int orderQuantity);
}
