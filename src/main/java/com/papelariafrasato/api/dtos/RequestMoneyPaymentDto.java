package com.papelariafrasato.api.dtos;

public record RequestMoneyPaymentDto(
        String userId,
        String orderId,
        Integer changeFor
){}