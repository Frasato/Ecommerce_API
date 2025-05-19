package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.RequestPaymentePixDto;
import com.papelariafrasato.api.services.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PayService payService;

    @PostMapping("/pix")
    public ResponseEntity<?> createPixPayment(@RequestBody RequestPaymentePixDto requestPixDto){
        return payService.generatePix(requestPixDto.customer(), requestPixDto.userId(), requestPixDto.orderId());
    }

}
