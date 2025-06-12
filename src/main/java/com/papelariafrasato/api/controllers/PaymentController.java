package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.RequestPaymentCardDto;
import com.papelariafrasato.api.dtos.ResponsePaymentCardDto;
import com.papelariafrasato.api.dtos.ResponsePaymentQueue;
import com.papelariafrasato.api.services.PaymentQueueService;
import com.papelariafrasato.api.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.Instant;

@RestController
@RequestMapping("/payments")
@Tag(
        name = "Payment",
        description = "EndPoints to create and process payment requests"
)
public class PaymentController {

    @Autowired
    private PaymentQueueService paymentQueueService;

    @PostMapping("/card")
    @Operation(
            summary = "Card",
            description = "Create and process card payment request"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment process success", content = @Content(schema = @Schema(implementation = ResponsePaymentCardDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information"),
    })
    public ResponseEntity<?> cardPayment(@RequestBody RequestPaymentCardDto paymentCardDto){
        paymentQueueService.sendPaymentToQueue(paymentCardDto);
        return ResponseEntity.accepted().body(new ResponsePaymentQueue(Instant.now().toString(), "Payment receive and will be process later..."));
    }

}
