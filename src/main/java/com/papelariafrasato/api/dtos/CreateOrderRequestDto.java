package com.papelariafrasato.api.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequestDto(
        @NotBlank(message = "UserId shouldn't be empty")
        String userId
){} 