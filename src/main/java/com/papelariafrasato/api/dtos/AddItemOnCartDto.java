package com.papelariafrasato.api.dtos;

public record AddItemOnCartDto(
        String cartId,
        String userId,
        String productName,
        String productDescription,
        double price,
        String category
) {}
