package com.papelariafrasato.api.repositories;

import com.papelariafrasato.api.models.ProductAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductAnalyticsRepository extends JpaRepository<ProductAnalytics, String> {
    @Query(value = "SELECT * FROM products_analytics WHERE product_id = :productId", nativeQuery = true)
    Optional<ProductAnalytics> findByProductId(@Param("productId")String productId);

    @Query(value = "SELECT * FROM products_analytics WHERE purchase > 1 ORDER BY purchase,date_time LIMIT 15", nativeQuery = true)
    List<ProductAnalytics> findByPurchase();

    @Query(value = "SELECT * FROM products_analytics WHERE click > 1 ORDER BY click,date_time LIMIT 15", nativeQuery = true)
    List<ProductAnalytics> findByClick();

    @Query(value = "SELECT * FROM products_analytics WHERE cart_added > 1 ORDER BY cart_added,date_time LIMIT 15", nativeQuery = true)
    List<ProductAnalytics> findByCartAdded();
}
