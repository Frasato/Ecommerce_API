package com.papelariafrasato.api.services;

import com.papelariafrasato.api.models.Cart;
import com.papelariafrasato.api.models.CartItem;
import com.papelariafrasato.api.models.Product;
import com.papelariafrasato.api.models.User;
import com.papelariafrasato.api.repositories.CartItemRepository;
import com.papelariafrasato.api.repositories.CartRepository;
import com.papelariafrasato.api.repositories.ProductRepository;
import com.papelariafrasato.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<?> findAllCartItemsUser(String userId){
        try {

            Cart cart = cartRepository.findCartByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Cart not found or user not logger"));
            return ResponseEntity.ok().body(cart);

        }catch(Exception exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

    public ResponseEntity<?> addItemOnCart(String userId, String productId){
        try{
            Optional<User> foundUser = userRepository.findById(userId);
            if(foundUser.isEmpty()) return ResponseEntity.badRequest().body("User not found or not logged!");

            Cart cart = cartRepository.findCartByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Cart doesn't exist"));

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not exist"));

            Optional<CartItem> foundCartItem = cartItemRepository.findByProductId(productId, cart.getId());
            if(foundCartItem.isPresent()){
                CartItem cartItem = foundCartItem.get();
                int quantity = cartItem.getQuantity() + 1;
                cartItem.setQuantity(quantity);

                int productPrice = product.getPriceWithDiscount() > 0? product.getPriceWithDiscount() : product.getPrice();
                int newTotalPrice = cart.getTotalPrice() + productPrice;
                cart.setTotalPrice(newTotalPrice);

                cartItemRepository.save(cartItem);
                cartRepository.save(cart);
                return ResponseEntity.ok().build();
            }

            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(1);

            int productPrice = product.getPriceWithDiscount() > 0? product.getPriceWithDiscount() : product.getPrice();
            int totalPrice = cart.getTotalPrice() + productPrice;
            cart.setTotalPrice(totalPrice);

            cartItemRepository.save(cartItem);
            cartRepository.save(cart);

            return ResponseEntity.status(201).build();

        }catch(Exception exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

    public ResponseEntity<?> removeItemFromCart(String cartItemId){
        Optional<CartItem> foundCart = cartItemRepository.findById(cartItemId);
        if(foundCart.isPresent()){
            CartItem cartItem = foundCart.get();
            Cart cart = cartItem.getCart();
            Product product = cartItem.getProduct();

            int productPrice = product.getPriceWithDiscount() > 0? product.getPriceWithDiscount() : product.getPrice();
            int newTotalPrice = cart.getTotalPrice() - productPrice;
            cart.setTotalPrice(newTotalPrice);

            if(cartItem.getQuantity() > 1){
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                cartItemRepository.save(cartItem);
            }else{
                cartItemRepository.delete(cartItem);
            }

            cartRepository.save(cart);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().body("Cart Item doesn't exist");
    }

    public ResponseEntity<?> clearCart(String userId){
        try{

            Optional<Cart> foundCart = cartRepository.findCartByUserId(userId);
            if(foundCart.isPresent()){
                Cart cart = foundCart.get();
                cart.setCartItem(null);
                cart.setTotalPrice(0);
                cartRepository.save(cart);

                return ResponseEntity.ok().build();
            }

            return ResponseEntity.badRequest().body("Cart not found!");

        }catch(Exception exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }
}
