package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.AddItemToCartRequestDto;
import com.papelariafrasato.api.dtos.ResponseUserDto;
import com.papelariafrasato.api.exceptions.*;
import com.papelariafrasato.api.models.Cart;
import com.papelariafrasato.api.services.CartService;
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
            @ApiResponse(
                    responseCode = "200",
                    description = "Found and return the cart",
                    content = @Content(schema = @Schema(implementation = Cart.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cart not found",
                    content = @Content(schema = @Schema(implementation = CartNotFoundException.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "A internal server error occurred",
                    content = @Content(schema = @Schema(implementation = InternalServerException.class))),
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
            @ApiResponse(
                    responseCode = "201",
                    description = "Cart created and add item to the cart"),
            @ApiResponse(
                    responseCode = "200",
                    description = "Added item to the cart"),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = UserNotFoundException.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cart not found",
                    content = @Content(schema = @Schema(implementation = CartNotFoundException.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ProductNotFoundException.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "A internal server error occurred",
                    content = @Content(schema = @Schema(implementation = InternalServerException.class))),
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
            @ApiResponse(
                    responseCode = "200",
                    description = "Item removed of the cart"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cart Item not found",
                    content = @Content(schema = @Schema(implementation = CartItemNotFoundException.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "A internal server error occurred",
                    content = @Content(schema = @Schema(implementation = InternalServerException.class))),
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
            @ApiResponse(
                    responseCode = "200",
                    description = "The cart is cleaned"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cart not found",
                    content = @Content(schema = @Schema(implementation = CartNotFoundException.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "A internal server error occurred",
                    content = @Content(schema = @Schema(implementation = InternalServerException.class))),
    })
    public ResponseEntity<?> clearCart(@PathVariable("id")String userId){
        return cartService.clearCart(userId);
    }

    @PutMapping("/plus/{cartItemId}")
    @Operation(
            summary = "Plus One",
            description = "Get the cart item and plus one in the quantity of him"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Add 1 of the amount of the cart item"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cart Item not found",
                    content = @Content(schema = @Schema(implementation = CartItemNotFoundException.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "A internal server error occurred",
                    content = @Content(schema = @Schema(implementation = InternalServerException.class))),
    })
    public ResponseEntity<?> plusOneCartItem(@PathVariable("cartItemId")String cartItemId){
        return cartService.plusOneCartItem(cartItemId);
    }

    @PutMapping("/minus/{cartItemId}")
    @Operation(
            summary = "Minus One",
            description = "Get the cart item and minus one, if the cart item has just one item, he will be deleted"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Remove 1 of the amount of the cart item"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cart Item not found",
                    content = @Content(schema = @Schema(implementation = CartItemNotFoundException.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "A internal server error occurred",
                    content = @Content(schema = @Schema(implementation = InternalServerException.class))),
    })
    public ResponseEntity<?> minusOneCartItem(@PathVariable("cartItemId")String cartItemId){
        return cartService.minusOneCartItem(cartItemId);
    }

}
