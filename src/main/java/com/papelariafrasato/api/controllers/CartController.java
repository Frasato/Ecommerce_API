package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.AddItemToCartRequestDto;
import com.papelariafrasato.api.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@Tag(
        name = "Cart",
        description = "EndPoints to manipulate a user cart"
)
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Take cart",
            description = "Get items on cart by user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found cart"),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information")
    })
    public ResponseEntity<?> getCart(@PathVariable("id")String userId){
        return cartService.findAllCartItemsUser(userId);
    }

    @PostMapping()
    @Operation(
            summary = "Add item",
            description = "Add items on cart"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Added item on cart"),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information")
    })
    public ResponseEntity<?> addItem(@RequestBody AddItemToCartRequestDto itemDto){
        return cartService.addItemOnCart(itemDto.userId(), itemDto.productId());
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Remove",
            description = "Remove item on cart"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item was removed"),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information")
    })
    public ResponseEntity<?> removeItemFromCart(@PathVariable("id")String cartItemId){
        return cartService.removeItemFromCart(cartItemId);
    }

    @PutMapping("/clear/{id}")
    @Operation(
            summary = "Clear",
            description = "Clear the cart, remove all items on the cart"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cleaned"),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information")
    })
    public ResponseEntity<?> clearCart(@PathVariable("id")String userId){
        return cartService.clearCart(userId);
    }

}
