package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.AddItemOnCartDto;
import com.papelariafrasato.api.dtos.RemoveItemFromCartDto;
import com.papelariafrasato.api.models.Cart;
import com.papelariafrasato.api.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Cart>> itensCartByUser(@RequestParam("userId") String userId){
        List<Cart> itens = cartService.findAllCartProductsOfUser(userId);

        if(itens == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(itens);
    }

    @PostMapping("/add")
    public ResponseEntity<?> saveItemOnCart(@RequestBody AddItemOnCartDto addItemOnCartDto){
        return cartService.saveItensOnCart(
                addItemOnCartDto.cartId(),
                addItemOnCartDto.userId(),
                addItemOnCartDto.productName(),
                addItemOnCartDto.productDescription(),
                addItemOnCartDto.price(),
                addItemOnCartDto.category()
        );
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeItemFromCart(@RequestBody RemoveItemFromCartDto removeItemFromCartDto){
        return cartService.removeItemCart(
                removeItemFromCartDto.productId(),
                removeItemFromCartDto.cartId(),
                removeItemFromCartDto.userId()
        );
    }
}
