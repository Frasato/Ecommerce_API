package com.papelariafrasato.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AddItemToCartRequestDto(
        @NotBlank(message = "UserId shouldn't be empty")
        @Schema(description = "UUID user id", example = "adaw331-asdaf31-242fr2-qd31fe33")
        String userId,
        @Schema(description = "UUID product id", example = "adaw331-ffke59-242fr2-zxxl990")
        String productId
){}
