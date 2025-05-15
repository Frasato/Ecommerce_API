package com.papelariafrasato.api.dtos;

public record RequestAddDiscountProductDto(
    String productId,
    int discount
){}
