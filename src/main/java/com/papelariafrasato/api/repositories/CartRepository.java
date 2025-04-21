package com.papelariafrasato.api.repositories;

import com.papelariafrasato.api.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    @Query(value = "SELECT * FROM carts WHERE user_id = :user_id", nativeQuery = true)
    List<Cart> findAllByUserId(@Param("user_id") String userId);
}
