package com.papelariafrasato.api.dtos;

public record RemoveItemFromCartDto(
        String cartId,
        String userId,
        String productId
) {}
