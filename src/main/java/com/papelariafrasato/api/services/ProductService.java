package com.papelariafrasato.api.services;

import com.papelariafrasato.api.models.Product;
import com.papelariafrasato.api.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public void createProduct(String productName, String productDescription, double price, String category){
        Product product = new Product();

        product.setProduct_name(productName);
        product.setProduct_description(productDescription);
        product.setPrice(price);
        product.setCategory(category);
        product.setDiscount(0);

        productRepository.save(product);
    }

    public void addDiscountOnProduct(String productId, double discount){
        try{
            Optional<Product> findedProduct = productRepository.findById(productId);

            if(findedProduct.isPresent()){
                Product product = findedProduct.get();
                product.setDiscount(discount);
                productRepository.save(product);
            }
        }catch(Exception exception){
            throw new RuntimeException("ERROR: " + exception.getMessage());
        }

    }
}
