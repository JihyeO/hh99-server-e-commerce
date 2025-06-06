package kr.hhplus.be.server.product;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  public record ProductResponse(Long id, String name, BigDecimal price) {
    public static ProductResponse from(Product product) {
      return new ProductResponse(
        product.getId(),
        product.getName(),
        product.getPrice()
      );
    }
  }

  public record TopProductResponse(Long id, String name, Long totalSold) {
    public static TopProductResponse from(TopProductProjection productProjection) {
        return new TopProductResponse(
            productProjection.getProductId(),
            productProjection.getProductName(),
            productProjection.getTotalSold()
        );
    }
  }

  @GetMapping
  public ResponseEntity<List<ProductResponse>> getProducts() {
    List<Product> products = productService.getProducts();
    List<ProductResponse> response = products.stream()
      .map(ProductResponse::from)
      .toList();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/top-selling")
  public ResponseEntity<List<TopProductResponse>> getTopSellingProducts() {
    List<TopProductProjection> topProductProjections = productService.getTopSellingProducts();
    List<TopProductResponse> response = topProductProjections.stream()
      .map(TopProductResponse::from)
      .toList();
    return ResponseEntity.ok(response);
  }
}
