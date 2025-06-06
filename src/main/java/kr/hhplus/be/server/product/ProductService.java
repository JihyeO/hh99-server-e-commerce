package kr.hhplus.be.server.product;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ProductService {
  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public List<Product> getProducts() {
    return productRepository.findAll();
  }

  public List<TopProductProjection> getTopSellingProducts() {
    return productRepository.findTopSellingProducts();
  }
}
