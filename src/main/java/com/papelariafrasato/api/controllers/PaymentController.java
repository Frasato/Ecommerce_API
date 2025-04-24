package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllPayments(){
        return ResponseEntity.ok(paymentService.listAllPayments());
    }

}
