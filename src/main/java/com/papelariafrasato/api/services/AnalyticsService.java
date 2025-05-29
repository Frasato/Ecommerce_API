package com.papelariafrasato.api.services;

import com.papelariafrasato.api.repositories.ProductAnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    @Autowired
    private ProductAnalyticsRepository analyticsRepository;

    public ResponseEntity<?> getAllAnalytics(){
        return ResponseEntity.ok().body(analyticsRepository.findAll());
    }

    public ResponseEntity<?> getByPurchase(){
        return ResponseEntity.ok().body(analyticsRepository.findByPurchase());
    }

    public ResponseEntity<?> getByClick(){
        return ResponseEntity.ok().body(analyticsRepository.findByClick());
    }

    public ResponseEntity<?> getByCartAdded(){
        return ResponseEntity.ok().body(analyticsRepository.findByCartAdded());
    }
}
