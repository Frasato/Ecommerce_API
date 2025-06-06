package com.papelariafrasato.api.services;

import com.papelariafrasato.api.exceptions.CartItemNotFoundException;
import com.papelariafrasato.api.exceptions.CartNotFoundException;
import com.papelariafrasato.api.exceptions.ProductNotFoundException;
import com.papelariafrasato.api.exceptions.UserNotFoundException;
import com.papelariafrasato.api.models.Cart;
import com.papelariafrasato.api.models.CartItem;
import com.papelariafrasato.api.models.Product;
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
    @Autowired
    private ProductAnalyticsService analyticsService;

    @Transactional
    public ResponseEntity<?> findAllCartItemsUser(String userId) {
        Cart cart = cartRepository.findCartByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));
        return ResponseEntity.ok().body(cart);
    }

    @Transactional
    public ResponseEntity<?> addItemOnCart(String userId, String productId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        Cart cart = cartRepository.findCartByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Optional<CartItem> foundCartItem = cartItemRepository.findByProductId(productId, cart.getId());
        if (foundCartItem.isPresent()) {
            CartItem cartItem = foundCartItem.get();
            plusOneCartItem(cartItem.getId());

            analyticsService.cartAddedProduct(productId);
            return ResponseEntity.ok().build();
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

        analyticsService.cartAddedProduct(productId);

        return ResponseEntity.status(201).build();
    }

    @Transactional
    public ResponseEntity<?> removeItemFromCart(String cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        Cart cart = cartItem.getCart();
        Product product = cartItem.getProduct();

        int productPrice = product.getPriceWithDiscount() > 0 ? product.getPriceWithDiscount() : product.getPrice();
        int totalItemPrice = productPrice * cartItem.getQuantity();
        int newTotalPrice = cart.getTotalPrice() - totalItemPrice;
        cart.setTotalPrice(newTotalPrice);

        cartItemRepository.delete(cartItem);
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

    @Transactional
    public ResponseEntity<?> plusOneCartItem(String cartItemId){
        Optional<CartItem> foundedCartItem = cartItemRepository.findById(cartItemId);

        if(foundedCartItem.isPresent()){
            CartItem cartItem = foundedCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);

            Cart cart = cartItem.getCart();
            Product product = cartItem.getProduct();

            int productPrice = product.getPriceWithDiscount() > 0? product.getPriceWithDiscount() : product.getPrice();
            cart.setTotalPrice(cart.getTotalPrice() + productPrice);

            cartItemRepository.save(cartItem);
            cartRepository.save(cart);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<?> minusOneCartItem(String cartItemId){
        Optional<CartItem> foundedCartItem = cartItemRepository.findById(cartItemId);

        if(foundedCartItem.isPresent()){
            CartItem cartItem = foundedCartItem.get();
            Cart cart = cartItem.getCart();
            Product product = cartItem.getProduct();
            int productPrice = product.getPriceWithDiscount() > 0? product.getPriceWithDiscount() : product.getPrice();

            if(cartItem.getQuantity() == 1){
                return removeItemFromCart(cartItemId);
            }
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cart.setTotalPrice(cart.getTotalPrice() - productPrice);

            cartRepository.save(cart);
            cartItemRepository.save(cartItem);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
