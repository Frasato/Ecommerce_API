package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.RequestCardPaymentDto;
import com.papelariafrasato.api.dtos.RequestPaymentePixDto;
import com.papelariafrasato.api.services.PayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/payment")
@Tag(
        name = "Payment",
        description = "EndPoints to pay orders"
)
public class PaymentController {

    @Autowired
    private PayService payService;

    @PostMapping("/pix")
    @Operation(
            summary = "Create Pix",
            description = "Create pix qrcode and copy and paste"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created pix"),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information")
    })
    public ResponseEntity<?> createPixPayment(@RequestBody RequestPaymentePixDto requestPixDto){
        try {
            return payService.generatePix(requestPixDto.userId(), requestPixDto.orderId());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/card")
    @Operation(
            summary = "Create Card",
            description = "Create card payment"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created card requisition"),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information")
    })
    public ResponseEntity<?> createCardPayment(@RequestBody RequestCardPaymentDto cardPaymentDto){
        try {
            return payService.cardPaymente(cardPaymentDto.userId(),cardPaymentDto.orderId(), cardPaymentDto.parcel());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
