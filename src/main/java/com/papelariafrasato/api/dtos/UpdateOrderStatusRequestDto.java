package com.papelariafrasato.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateOrderStatusRequestDto(
        @Schema(description = "Order status", example = "PENDING")
        String status
){} 