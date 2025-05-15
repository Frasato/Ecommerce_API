package com.papelariafrasato.api.services;

import com.papelariafrasato.api.models.Product;
import com.papelariafrasato.api.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<?> allProducts(){
        try {
            return ResponseEntity.ok().body(productRepository.findAll());
        }catch(Exception exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

    public ResponseEntity<?> addProduct(String image, String name, String description, Integer price, String category){
        try{

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

        }catch(Exception exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

    public ResponseEntity<?> addDiscountOnProduct(String productId, int discount){
        try{

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found!"));

            product.setDiscount(discount);
            final int newPrice = product.getPrice() - discount;
            product.setPriceWithDiscount(newPrice);

            productRepository.save(product);
            return ResponseEntity.ok().build();

        }catch(Exception exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

    public ResponseEntity<?> removeProductDiscount(String productId){
        try {

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            product.setDiscount(0);
            product.setPriceWithDiscount(0);
            productRepository.save(product);
            return ResponseEntity.ok().build();

        }catch(Exception exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

    public ResponseEntity<?> removeProduct(String productId){
        try {

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found!"));
            productRepository.delete(product);
            return ResponseEntity.ok().build();

        }catch(Exception exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

}
