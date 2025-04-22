package com.papelariafrasato.api.repositories;

import com.papelariafrasato.api.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

    @Query(value = "SELECT * FROM order_item WHERE order_id = :order_id", nativeQuery = true)
    List<OrderItem> findOrderItemByIdOrderId(@Param("order_id")String orderId);
}
