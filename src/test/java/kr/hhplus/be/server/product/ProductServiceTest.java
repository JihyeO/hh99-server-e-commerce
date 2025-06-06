package kr.hhplus.be.server.product;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {
  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private ProductService productService;

  private void setId(Product product, Long id) throws Exception {
    Field idField = Product.class.getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(product, id);
  }

  @Test
  public void testGetProducts() throws Exception {
    Product mockProduct1 = new Product("Shirts", new BigDecimal("20000"), 100);
    setId(mockProduct1, 1L);
    Product mockProduct2 = new Product("Pants", new BigDecimal("25000"), 100);
    setId(mockProduct2, 2L);

    List<Product> mockProducts = Arrays.asList(mockProduct1, mockProduct2);

    when(productRepository.findAll()).thenReturn(mockProducts);

    List<Product> result = productService.getProducts();

    assertThat(result).hasSize(2);
    assertThat(result).containsExactly(mockProduct1, mockProduct2);
  }
}