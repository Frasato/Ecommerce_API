package com.papelariafrasato.api.dtos;

import jakarta.validation.constraints.NotBlank;

public record AddItemToCartRequestDto(
        @NotBlank(message = "UserId shouldn't be empty")
        String userId,
        @NotBlank(message = "ProductId shouldn't be empty")
        String productId
){}
