package com.papelariafrasato.api.dtos;

import jakarta.validation.constraints.NotBlank;

public record RequestPaymentePixDto(
        String userId,
        String orderId) {
}
