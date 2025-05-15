package com.papelariafrasato.api.repositories;

import com.papelariafrasato.api.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String>{
    @Query(value = "SELECT * FROM cart_items WHERE product_id = :productId AND cart_id = :cartId", nativeQuery = true)
    Optional<CartItem> findByProductId(@Param("productId")String productId, @Param("cartId")String cartId);
}
