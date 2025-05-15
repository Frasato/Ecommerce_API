package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.RequestAddDiscountProductDto;
import com.papelariafrasato.api.dtos.RequestProductDto;
import com.papelariafrasato.api.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping()
    public ResponseEntity<?> getAllProducts(){
        return productService.allProducts();
    }

    @PostMapping()
    public ResponseEntity<?> createNewProduct(@RequestBody RequestProductDto productDto){
        return productService.addProduct(
                productDto.image(),
                productDto.name(),
                productDto.description(),
                productDto.price(),
                productDto.category()
        );
    }

    @PutMapping()
    public ResponseEntity<?> addDiscount(@RequestBody RequestAddDiscountProductDto discountProductDto){
        return productService.addDiscountOnProduct(discountProductDto.productId(), discountProductDto.discount());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> removeDiscount(@PathVariable("id") String productId){
        return productService.removeProductDiscount(productId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") String productId){
        return productService.removeProduct(productId);
    }

}
