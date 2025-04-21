package com.papelariafrasato.api.services;

import com.papelariafrasato.api.models.Cart;
import com.papelariafrasato.api.models.Product;
import com.papelariafrasato.api.models.User;
import com.papelariafrasato.api.repositories.CartRepository;
import com.papelariafrasato.api.repositories.ProductRepository;
import com.papelariafrasato.api.repositories.UserRepository;
import org.hibernate.query.QueryArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<Cart> findAllCartProductsOfUser(String userId){
        List<Cart> findedProducts = cartRepository.findAllByUserId(userId);

        if(findedProducts.isEmpty()){
            return null;
        }

        return findedProducts;
    }

    public ResponseEntity<?> saveItensOnCart(String cartId, String userId, String productName, String productDescription, double price, String category){
        try{
            Optional<User> findedUser = userRepository.findById(userId);
            Optional<Cart> findedCart = cartRepository.findById(cartId);

            if(findedUser.isEmpty()){
                return ResponseEntity.badRequest().body("ERROR: User not found!");
            }
            if(findedCart.isEmpty()){
                Cart cart = new Cart();
                User user = findedUser.get();
                List<Product> products = new ArrayList<>();

                Product product = new Product();
                product.setCategory(category);
                product.setPrice(price);
                product.setProduct_name(productName);
                product.setProduct_description(productDescription);

                products.add(product);

                cart.setProducts(products);
                user.setCart(cart);

                cartRepository.save(cart);
                userRepository.save(user);
                return ResponseEntity.ok().build();
            }

            List<Product> findedsProducts = findedCart.get().getProducts();
            Cart cart = findedCart.get();
            User user = findedUser.get();

            Product product = new Product();
            product.setProduct_name(productName);
            product.setProduct_description(productDescription);
            product.setPrice(price);
            product.setCategory(category);

            findedsProducts.add(product);
            cart.setProducts(findedsProducts);
            user.setCart(cart);

            cartRepository.save(cart);
            userRepository.save(user);
            return ResponseEntity.ok().build();
        }catch (QueryArgumentException queryArgumentException){
            throw  new RuntimeException("ERROR: " + queryArgumentException);
        }
    }

    public ResponseEntity<?> removeItemCart(String productId, String cartId, String userId){
        try {
            Optional<Product> findedProduct = productRepository.findById(productId);
            Optional<Cart> findedCart = cartRepository.findById(cartId);
            Optional<User> findedUser = userRepository.findById(userId);

            if(findedCart.isEmpty() || findedProduct.isEmpty() || findedUser.isEmpty()){
                return ResponseEntity.badRequest().body("ERROR: cart or product or user is empty!");
            }

            Cart cart = findedCart.get();
            User user = findedUser.get();
            Product product = findedProduct.get();

            List<Product> productsList = cart.getProducts();
            productsList.remove(product);

            cart.setProducts(productsList);
            user.setCart(cart);

            userRepository.save(user);
            cartRepository.save(cart);
            return ResponseEntity.ok().build();
        }catch (QueryArgumentException queryArgumentException){
            throw new RuntimeException("ERROR: " + queryArgumentException);
        }
    }

}
