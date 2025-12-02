package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.*;
import com.papelariafrasato.api.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Tag(
        name = "Order",
        description = "EndPoints to create orders and see"
)
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping()
    @Operation(
            summary = "All Orders",
            description = "Get all orders"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success")
    })
    public ResponseEntity<?> allOrders(){
        return orderService.getAllOrders();
    }

    @PostMapping()
    @Operation(
            summary = "Create",
            description = "Create product order"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created order", content = @Content(schema = @Schema(implementation = ResponseOrderDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information")
    })
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequestDto requestDto) {
        return orderService.createOrder(requestDto.userId(), requestDto.deliveryOption());
    }

    @PostMapping("/direct")
    @Operation(
            summary = "Create",
            description = "Create order for a only one product"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created order", content = @Content(schema = @Schema(implementation = ResponseOrderDto.class))),
    })
    public ResponseEntity<?> createOrderDirect(@RequestBody RequestDirectOrderDto requestDto) {
        return orderService.createOrderOnlyOneProduct(requestDto.productId(), requestDto.userId(), requestDto.deliveryPrice());
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Take all order by ID",
            description = "Get orders by user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found user orders", content = @Content(schema = @Schema(implementation = ResponseAllOrdersDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information")
    })
    public ResponseEntity<?> getAllOrders(@PathVariable("userId") String userId) {
        return orderService.getAllOrdersById(userId);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Take order",
            description = "Get order by id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found user order by order id", content = @Content(schema = @Schema(implementation = ResponseOrderDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information")
    })
    public ResponseEntity<?> getOrderById(@PathVariable("id") String orderId) {
        return orderService.getOrderById(orderId);
    }

    @PutMapping("/stauts/{id}")
    public ResponseEntity<String> actualizeStatus(@PathVariable("id") String orderId){
        return orderService.updateOrderStatus(orderId);
    }
} 