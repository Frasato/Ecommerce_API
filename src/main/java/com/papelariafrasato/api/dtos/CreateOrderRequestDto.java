package com.papelariafrasato.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequestDto(
        @NotBlank(message = "UserId shouldn't be empty")
        @Schema(description = "UUID user id", example = "adaw331-asdaf31-242fr2-qd31fe33")
        String userId,
        @NotBlank(message = "Delivery Option shouldn't be empty")
        @Schema(description = "Delivery option to calculate and generate ticket", example = "1")
        Integer deliveryOption
){} 