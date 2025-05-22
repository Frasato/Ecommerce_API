package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.AddItemToCartRequestDto;
import com.papelariafrasato.api.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCart(@PathVariable("id")String userId){
        return cartService.findAllCartItemsUser(userId);
    }

    @PostMapping()
    public ResponseEntity<?> addItem(@RequestBody AddItemToCartRequestDto itemDto){
        return cartService.addItemOnCart(itemDto.userId(), itemDto.productId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable("id")String cartItemId){
        return cartService.removeItemFromCart(cartItemId);
    }

    @PutMapping("/clear/{id}")
    public ResponseEntity<?> clearCart(@PathVariable("id")String userId){
        return cartService.clearCart(userId);
    }

}
