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
    List<Optional<Order>> findByAStatus(@Param("status") String status);
}
