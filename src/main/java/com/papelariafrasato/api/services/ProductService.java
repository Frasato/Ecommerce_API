package com.papelariafrasato.api.services;

import com.papelariafrasato.api.exceptions.InvalidPriceException;
import com.papelariafrasato.api.exceptions.ProductNotFoundException;
import com.papelariafrasato.api.models.Product;
import com.papelariafrasato.api.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<?> allProducts() {
        return ResponseEntity.ok().body(productRepository.findAll());
    }

    @Transactional
    public ResponseEntity<?> addProduct(String image, String name, String description, Integer price, String category) {
        if (price < 0) {
            throw new InvalidPriceException();
        }

        Product product = new Product();
        product.setImage(image);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setDiscount(0);
        product.setPriceWithDiscount(0);
        product.setCategory(category);

        productRepository.save(product);
        return ResponseEntity.status(201).build();
    }

    @Transactional
    public ResponseEntity<?> addDiscountOnProduct(String productId, int discount) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (discount < 0) {
            throw new InvalidPriceException();
        }

        if (discount > product.getPrice()) {
            throw new InvalidPriceException();
        }

        product.setDiscount(discount);
        final int newPrice = product.getPrice() - discount;
        product.setPriceWithDiscount(newPrice);

        productRepository.save(product);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<?> removeProductDiscount(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        
        product.setDiscount(0);
        product.setPriceWithDiscount(0);
        productRepository.save(product);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<?> removeProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        
        productRepository.delete(product);
        return ResponseEntity.ok().build();
    }
}
