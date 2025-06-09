package com.papelariafrasato.api.dtos;

public record RequestPaymentCardDto(
        String userId,
        String orderId,
        String cardName,
        String cardNumber,
        String expirationMonth,
        String expirationYear,
        String cvv,
        Integer installments
){}
