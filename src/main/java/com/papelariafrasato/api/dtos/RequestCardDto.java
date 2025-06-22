package com.papelariafrasato.api.dtos;

public record RequestCardDto(
        String userId,
        String orderId,
        String cardToken,
        Integer installments,
        String paymentMethodId
){}
