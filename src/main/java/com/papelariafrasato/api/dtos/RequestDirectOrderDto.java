package com.papelariafrasato.api.dtos;

public record RequestDirectOrderDto(
        String userId,
        String productId,
        Integer deliveryPrice
){}
