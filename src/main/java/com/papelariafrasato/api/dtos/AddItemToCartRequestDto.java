package com.papelariafrasato.api.dtos;

public record AddItemToCartRequestDto(
        String userId,
        String productId
){}
