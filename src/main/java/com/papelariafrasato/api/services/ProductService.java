package com.papelariafrasato.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${BLOB_URL}")
    private String blobStorageUrl;

    public ResponseEntity<?> allProducts() {
        return ResponseEntity.ok().body(productRepository.findAll());
    }

    @Transactional
    public ResponseEntity<?> addProduct(MultipartFile image, String name, String description, Integer price, String category){
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
            product.setName(name);
            product.setDescription(description);
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
