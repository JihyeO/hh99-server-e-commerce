package kr.hhplus.be.server.product.infrastructure;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import kr.hhplus.be.server.product.Product;
import kr.hhplus.be.server.product.ProductReader;
import kr.hhplus.be.server.product.ProductRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductReaderImpl implements ProductReader {
  private final ProductRepository productRepository;

  public List<Product> findAllById(List<Long> ids) {
    return productRepository.findAllById(ids);
  }

  public boolean hasEnoughProduct(Long productId, int orderQuantity) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다."));

    int newQuantity = product.getQuantity() - orderQuantity;
    return newQuantity < 0;
  }

  public void deductProduct(Long productId, int orderQuantity) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다."));

    int newQuantity = product.getQuantity() - orderQuantity;
    if (newQuantity < 0) {
        throw new IllegalStateException("재고가 부족합니다.");
    }

    product.setQuantity(newQuantity);
    productRepository.save(product);
  }
}
