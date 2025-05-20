package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.RequestCardPaymentDto;
import com.papelariafrasato.api.dtos.RequestPaymentePixDto;
import com.papelariafrasato.api.services.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PayService payService;

    @PostMapping("/pix")
    public ResponseEntity<?> createPixPayment(@RequestBody RequestPaymentePixDto requestPixDto){
        try {
            return payService.generatePix(requestPixDto.userId(), requestPixDto.orderId());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/card")
    public ResponseEntity<?> createCardPayment(@RequestBody RequestCardPaymentDto cardPaymentDto){
        try {
            return payService.cardPaymente(cardPaymentDto.userId(),cardPaymentDto.orderId(), cardPaymentDto.parcel());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
