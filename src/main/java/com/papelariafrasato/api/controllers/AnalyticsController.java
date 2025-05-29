package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.services.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytic")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping()
    public ResponseEntity<?> getAllAnalytics(){
        return analyticsService.getAllAnalytics();
    }

    @GetMapping("/purchase")
    public ResponseEntity<?> getAllByPurchase(){
        return analyticsService.getByPurchase();
    }

    @GetMapping("/click")
    public ResponseEntity<?> getAllClick(){
        return analyticsService.getByClick();
    }

    @GetMapping("/cart")
    public ResponseEntity<?> getAllCartAdded(){
        return analyticsService.getByCartAdded();
    }
}
