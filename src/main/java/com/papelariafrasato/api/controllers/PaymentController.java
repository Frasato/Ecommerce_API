package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.RequestCardDto;
import com.papelariafrasato.api.dtos.RequestMoneyPaymentDto;
import com.papelariafrasato.api.dtos.RequestPixDto;
import com.papelariafrasato.api.services.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
@Tag(
        name = "Payment",
        description = "EndPoints to create and process payment requests"
)
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pix")
    public ResponseEntity<?> pixPayment(@RequestBody RequestPixDto pixDto){
        return paymentService.pixPayment(pixDto);
    }

    @PostMapping("/card")
    public ResponseEntity<?> cardPayment(@RequestBody RequestCardDto cardDto) { return paymentService.cardPayment(cardDto); }

    @PostMapping("/money")
    public ResponseEntity<?> moneyPayment(@RequestBody RequestMoneyPaymentDto moneyDto){ return paymentService.moneyPayment(moneyDto); }
}