package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.ProductDto;
import com.papelariafrasato.api.models.Product;
import com.papelariafrasato.api.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductDto productDto){
        try{
            if(
                    productDto.productName().isEmpty() ||
                            productDto.productDescription().isEmpty() ||
                            productDto.category().isEmpty() ||
                            productDto.price() == 0
            ){
                return ResponseEntity.badRequest().body("ERROR: Some fields are empty!");
            }

            productService.createProduct(
                    productDto.productName(),
                    productDto.productDescription(),
                    productDto.price(),
                    productDto.category()
            );

            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
}
