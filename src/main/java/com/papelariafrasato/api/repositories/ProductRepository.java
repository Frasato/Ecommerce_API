package com.papelariafrasato.api.repositories;

import com.papelariafrasato.api.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>{
    @Query(value = "SELECT * FROM products WHERE category = :category", nativeQuery = true)
    List<Product> getProductsByCategory(@Param("category")String category);
}
