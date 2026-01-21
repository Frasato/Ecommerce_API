package com.papelariafrasato.api.services;

import com.papelariafrasato.api.models.Product;
import com.papelariafrasato.api.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class DiscountScheduleService {

    @Autowired
    private ProductRepository productRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void removeExpiredDiscounts() {
        LocalDate now = LocalDate.now();

        List<Product> expiredProducts = productRepository.findExpiredDiscounts(now);

        expiredProducts.forEach(product -> {
            product.setDiscount(0);
            product.setPriceWithDiscount(0);
            product.setDiscountEnd(null);
            product.setDiscountInit(null);
        });

        productRepository.saveAll(expiredProducts);
    }
}