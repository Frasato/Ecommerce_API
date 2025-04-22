package com.papelariafrasato.api.repositories;

import com.papelariafrasato.api.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Query(value = "SELECT * FROM orders WHERE orders.status = :status", nativeQuery = true)
    List<Order> findByAStatus(@Param("status") String status);

    @Query(value = "SELECT * FROM orders WHERE orders.user_id = :user_id", nativeQuery = true)
    List<Order> findByUserId(@Param("user_id") String user_id);

    @Query(value = "SELECT * FROM orders WHERE orders.user_id = :user_id AND orders.status = 'open'", nativeQuery = true)
    Optional<Order> findOrderByUserId(@Param("user_id")String user_id);
}
