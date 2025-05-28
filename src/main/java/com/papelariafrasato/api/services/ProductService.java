package com.papelariafrasato.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.papelariafrasato.api.dtos.ResponseAllProductsDto;
import com.papelariafrasato.api.exceptions.InvalidPriceException;
import com.papelariafrasato.api.exceptions.ProductNotFoundException;
import com.papelariafrasato.api.models.Product;
import com.papelariafrasato.api.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.blob.storage.api}")
    private String blobStorageUrl;

    public ResponseEntity<?> allProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok().body(new ResponseAllProductsDto(products));
    }

    @Transactional
    public ResponseEntity<?> addProduct(MultipartFile image, String barCode, String name, String description, String producer, Integer price, String category){
        try {
            if (price < 0) {
                return ResponseEntity.badRequest().body("The price must be more than 100");
            }

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new org.springframework.core.io.ByteArrayResource(image.getBytes()){
                @Override
                public String getFilename(){
                    return image.getOriginalFilename();
                }
            });
            body.add("name", name);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            String response = restTemplate.postForObject(blobStorageUrl, requestEntity, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            String urlImage = root.get("url").asText();

            Product product = new Product();
            product.setImage(urlImage);
            product.setBarCode(barCode);
            product.setName(name);
            product.setDescription(description);
            product.setProducer(producer);
            product.setPrice(price);
            product.setDiscount(0);
            product.setPriceWithDiscount(0);
            product.setCategory(category);

            productRepository.save(product);
            return ResponseEntity.status(201).build();
        }catch(IOException exception){
            return ResponseEntity.internalServerError().body("ERROR HERE: " + exception.getMessage());
        }
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
        return ResponseEntity.status(204).build();
    }

    @Transactional
    public ResponseEntity<?> removeProductDiscount(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        product.setDiscount(0);
        product.setPriceWithDiscount(0);
        productRepository.save(product);
        return ResponseEntity.status(204).build();
    }

    @Transactional
    public ResponseEntity<?> addDiscountOnCategory(String category, int discount){
        try{
            if(category.isEmpty() || discount <= 0){
                return ResponseEntity.badRequest().body("ERROR: Category or Discount doesn't be empty!");
            }

            List<Product> products = productRepository.getProductsByCategory(category);
            double percent = (double) discount / 100;

            for(Product product : products){
                int newPrice = (int) (product.getPrice() * (1 - percent));
                product.setPriceWithDiscount(newPrice);
                productRepository.save(product);
            }

            return ResponseEntity.status(204).build();
        }catch(RuntimeException exception){
            return ResponseEntity.internalServerError().body("ERROR: " + exception.getMessage());
        }

    }

    @Transactional
    public ResponseEntity<?> removeProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        
        productRepository.delete(product);
        return ResponseEntity.ok().build();
    }
}
