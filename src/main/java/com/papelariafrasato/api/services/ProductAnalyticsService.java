package com.papelariafrasato.api.services;

import com.papelariafrasato.api.exceptions.ProductNotFoundException;
import com.papelariafrasato.api.models.Order;
import com.papelariafrasato.api.models.OrderItem;
import com.papelariafrasato.api.models.Product;
import com.papelariafrasato.api.models.ProductAnalytics;
import com.papelariafrasato.api.repositories.OrderItemRepository;
import com.papelariafrasato.api.repositories.OrderRepository;
import com.papelariafrasato.api.repositories.ProductAnalyticsRepository;
import com.papelariafrasato.api.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ProductAnalyticsService {

    @Autowired
    private ProductAnalyticsRepository analyticsRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public void purchaseProduct(Order order){
        Optional<Order> foundedOrder = orderRepository.findById(order.getId());
        Order getOrder = foundedOrder.get();

        for(OrderItem orderItem : getOrder.getOrderItems()){
            Product product = orderItem.getProduct();
            Optional<ProductAnalytics> foundedAnalytics = analyticsRepository.findByProductId(product.getId());

            if(foundedAnalytics.isPresent()){
                ProductAnalytics productAnalytics = foundedAnalytics.get();
                productAnalytics.setPurchase(productAnalytics.getPurchase() + 1);
                analyticsRepository.save(productAnalytics);
            }

            ProductAnalytics productAnalytics = new ProductAnalytics();
            productAnalytics.setPurchase(1);
            productAnalytics.setProduct(product);
            analyticsRepository.save(productAnalytics);
        }
    }

    public void clickedProduct(String productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Optional<ProductAnalytics> analytics = analyticsRepository.findByProductId(productId);

        if(analytics.isPresent()){
            ProductAnalytics productAnalytics = analytics.get();
            productAnalytics.setClick(productAnalytics.getClick() + 1);
            analyticsRepository.save(productAnalytics);
        }

        ProductAnalytics productAnalytics = new ProductAnalytics();
        productAnalytics.setProduct(product);
        productAnalytics.setClick(1);
        analyticsRepository.save(productAnalytics);
    }

    public void cartAddedProduct(String productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Optional<ProductAnalytics> analytics = analyticsRepository.findByProductId(productId);

        if(analytics.isPresent()){
            ProductAnalytics productAnalytics = analytics.get();
            productAnalytics.setCartAdded(productAnalytics.getCartAdded() + 1);
            analyticsRepository.save(productAnalytics);
        }

        ProductAnalytics productAnalytics = new ProductAnalytics();
        productAnalytics.setProduct(product);
        productAnalytics.setCartAdded(1);
        analyticsRepository.save(productAnalytics);
    }

}
