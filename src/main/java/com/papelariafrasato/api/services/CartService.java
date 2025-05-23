package com.papelariafrasato.api.services;

import com.papelariafrasato.api.exceptions.CartItemNotFoundException;
import com.papelariafrasato.api.exceptions.CartNotFoundException;
import com.papelariafrasato.api.exceptions.ProductNotFoundException;
import com.papelariafrasato.api.exceptions.UserNotFoundException;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public ResponseEntity<?> findAllCartItemsUser(String userId) {
        Cart cart = cartRepository.findCartByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));
        return ResponseEntity.ok().body(cart);
    }

    @Transactional
    public ResponseEntity<?> addItemOnCart(String userId, String productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Cart cart = cartRepository.findCartByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Optional<CartItem> foundCartItem = cartItemRepository.findByProductId(productId, cart.getId());
        if (foundCartItem.isPresent()) {
            CartItem cartItem = foundCartItem.get();
            int quantity = cartItem.getQuantity() + 1;
            cartItem.setQuantity(quantity);

            int productPrice = product.getPriceWithDiscount() > 0 ? product.getPriceWithDiscount() : product.getPrice();
            int newTotalPrice = cart.getTotalPrice() + productPrice;
            cart.setTotalPrice(newTotalPrice);

            cartItemRepository.save(cartItem);
            cartRepository.save(cart);
            return ResponseEntity.status(201).build();
        }

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setCart(cart);
        cartItem.setQuantity(1);

        int productPrice = product.getPriceWithDiscount() > 0 ? product.getPriceWithDiscount() : product.getPrice();
        int totalPrice = cart.getTotalPrice() + productPrice;
        cart.setTotalPrice(totalPrice);

        cartItemRepository.save(cartItem);
        cartRepository.save(cart);

        return ResponseEntity.status(201).build();
    }

    @Transactional
    public ResponseEntity<?> removeItemFromCart(String cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        Cart cart = cartItem.getCart();
        Product product = cartItem.getProduct();

        int productPrice = product.getPriceWithDiscount() > 0 ? product.getPriceWithDiscount() : product.getPrice();
        int newTotalPrice = cart.getTotalPrice() - productPrice;
        cart.setTotalPrice(newTotalPrice);

        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItemRepository.save(cartItem);
        } else {
            cartItemRepository.delete(cartItem);
        }

        cartRepository.save(cart);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<?> clearCart(String userId) {
        Cart cart = cartRepository.findCartByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        List<CartItem> cartItems = cart.getCartItem();
        if(cartItems != null && !cartItems.isEmpty()){
            cartItemRepository.deleteAll(cartItems);
            cart.getCartItem().clear();
        }

        cart.setTotalPrice(0);
        cartRepository.save(cart);

        return ResponseEntity.ok().build();
    }
}
