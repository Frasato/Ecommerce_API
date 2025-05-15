package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.CreateOrderRequestDto;
import com.papelariafrasato.api.dtos.UpdateOrderStatusRequestDto;
import com.papelariafrasato.api.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping()
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequestDto requestDto) {
        return orderService.createOrder(requestDto.userId());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllOrders(@PathVariable("userId") String userId) {
        return orderService.getAllOrders(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable("id") String orderId) {
        return orderService.getOrderById(orderId);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable("id") String orderId,
            @RequestBody UpdateOrderStatusRequestDto requestDto) {
        return orderService.updateOrderStatus(orderId, requestDto.status());
    }
} 