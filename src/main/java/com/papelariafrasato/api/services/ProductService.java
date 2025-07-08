package com.papelariafrasato.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.papelariafrasato.api.dtos.ResponseAllProductsDto;
import com.papelariafrasato.api.dtos.ResponseProductDto;
import com.papelariafrasato.api.exceptions.DiscountException;
import com.papelariafrasato.api.exceptions.InternalServerException;
import com.papelariafrasato.api.exceptions.InvalidPriceException;
import com.papelariafrasato.api.exceptions.ProductNotFoundException;
import com.papelariafrasato.api.models.Product;
import com.papelariafrasato.api.models.ProductAnalytics;
import com.papelariafrasato.api.repositories.ProductAnalyticsRepository;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.blob.storage.api}")
    private String blobStorageUrl;
    @Autowired
    private ProductAnalyticsService analyticsService;
    @Autowired
    private ProductAnalyticsRepository analyticsRepository;

    @Transactional
    public ResponseEntity<?> getProduct(String productId){
        analyticsService.clickedProduct(productId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        return ResponseEntity.ok().body(new ResponseProductDto(product));
    }

    public ResponseEntity<?> allProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok().body(new ResponseAllProductsDto(products));
    }

    public ResponseEntity<?> getPurchaseProducts(){
        List<ProductAnalytics> analytics = analyticsRepository.findByPurchase();
        List<Product> products = new ArrayList<>();

        for(ProductAnalytics productAnalytics : analytics){
            products.add(productAnalytics.getProduct());
        }

        return ResponseEntity.ok().body(new ResponseAllProductsDto(products));
    }

    @Transactional
    public ResponseEntity<?> getAllProductsByCategory(String category){
        List<Product> allFoundProducts = productRepository.getProductsByCategory(category);
        return ResponseEntity.status(200).body(new ResponseAllProductsDto(allFoundProducts));
    }

    @Transactional
    public ResponseEntity<?> addProduct(MultipartFile image, String barCode, String name, String description, String producer, Integer price, String category, Double height, Double width, Double product_length, Double weight){
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
            product.setProduct_length(product_length);
            product.setHeight(height);
            product.setWidth(width);
            product.setWeight(weight);

            productRepository.save(product);
            return ResponseEntity.status(201).build();
        }catch(IOException exception){
            return ResponseEntity.internalServerError().body("ERROR HERE: " + exception.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> addDiscountOnProduct(String productId, int discount) {
        try{
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));

            if (discount < 0 || discount > 100) throw new InvalidPriceException();

            if (product.getDiscount() > 0) throw new DiscountException(discount);

            product.setDiscount(discount);

            double percent = (double) discount / 100;
            double discountPrice = product.getPrice() * percent;
            double applyDiscount = product.getPrice() - discountPrice;
            int newPrice = (int) applyDiscount * 100;

            product.setPriceWithDiscount(newPrice);

            productRepository.save(product);
            return ResponseEntity.status(200).build();
        }catch(Exception e){
            throw new InternalServerException(e.getMessage());
        }
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
