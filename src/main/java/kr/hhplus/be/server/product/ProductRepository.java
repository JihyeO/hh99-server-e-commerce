package kr.hhplus.be.server.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
  @Query(
        value = """
            SELECT
              p.id AS productId,
              p.name AS productName,
              SUM(oi.quantity) AS totalSold
            FROM
              `order` o
            JOIN
              order_item oi ON o.id = oi.order_id
            JOIN
              product p ON oi.product_id = p.id
            WHERE
              o.order_date >= CURDATE() - INTERVAL 3 DAY
            GROUP BY
              p.id, p.name
            ORDER BY
              totalSold DESC
            LIMIT 5
        """,
        nativeQuery = true
    )
    List<TopProductProjection> findTopSellingProducts();
}
