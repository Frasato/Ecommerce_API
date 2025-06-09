package com.papelariafrasato.api.dtos;

public record ResponsePaymentCardDto(
         String status,
         String transactionId,
         String orderId,
         String referenceNum,
         String authCode,
         String message,
         String processorName,
         String creditCardLast4
){}
