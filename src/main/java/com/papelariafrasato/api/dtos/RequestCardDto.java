package com.papelariafrasato.api.dtos;

public record RequestCardDto(
        String userId,
        String orderId,
        Integer installments,
        String cardNumber,
        String cardholderName,
        String expirationMonth,
        String expirationYear,
        String securityCode
){}
