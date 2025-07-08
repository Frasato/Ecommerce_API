package com.papelariafrasato.api.services;

import com.papelariafrasato.api.exceptions.InvalidPriceException;
import com.papelariafrasato.api.exceptions.ProductNotFoundException;
import com.papelariafrasato.api.models.Product;
import com.papelariafrasato.api.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductAnalyticsService analyticsService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        Field field = ProductService.class.getDeclaredField("blobStorageUrl");
        field.setAccessible(true);
        field.set(productService, "http://fake-blob.com/upload");
    }

    @Test
    void whenAddProductWithValidPrice_thenSuccess() {
        MockMultipartFile image = new MockMultipartFile(
            "image",
            "test.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );
        String name = "Test Product";
        String barCode = "1545114587";
        String producer = "Test producer";
        String description = "Test Description";
        Integer price = 100;
        String category = "Test Category";
        double height = 10.0;
        double width = 10.0;
        double product_length = 15.4;
        double weight = 20.0;

        Product savedProduct = new Product();
        savedProduct.setId("1");
        savedProduct.setName(name);
        savedProduct.setPrice(price);

        String blobResponse = "{\"url\":\"http://fake-blob.com/image.jpg\"}";
        when(restTemplate.postForObject(anyString(), any(), eq(String.class))).thenReturn(blobResponse);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ResponseEntity<?> response = productService.addProduct(image, barCode, name, description, producer, price, category, height, width, product_length, weight);

        assertEquals(201, response.getStatusCode().value());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void whenAddProductWithNegativePrice_thenThrowException() {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
        String name = "Test Product";
        String barCode = "1545114587";
        String producer = "Test producer";
        String description = "Test Description";
        Integer price = -100;
        String category = "Test Category";
        double height = 10.0;
        double width = 10.0;
        double product_length = 15.4;
        double weight = 20.0;

        ResponseEntity<?> response = productService.addProduct(image, barCode, name, description, producer, price, category, height, width, product_length, weight);
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void whenGetProductById_thenSuccess() {
        String productId = "1";
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ResponseEntity<?> response = productService.getProduct(productId);

        assertEquals(200, response.getStatusCode().value());
        verify(analyticsService, times(1)).clickedProduct(productId);
    }

    @Test
    void whenGetProductByIdNotFound_thenThrowException() {
        String productId = "1";
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.getProduct(productId);
        });
    }

    @Test
    void whenAddDiscountWithValidAmount_thenSuccess() {
        String productId = "1";
        int discount = 20;
        Product product = new Product();
        product.setId(productId);
        product.setPrice(100);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ResponseEntity<?> response = productService.addDiscountOnProduct(productId, discount);

        assertEquals(204, response.getStatusCode().value());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void whenAddDiscountWithInvalidAmount_thenThrowException() {
        String productId = "1";
        int discount = 120;
        Product product = new Product();
        product.setId(productId);
        product.setPrice(100);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThrows(InvalidPriceException.class, () -> {
            productService.addDiscountOnProduct(productId, discount);
        });
    }
} 