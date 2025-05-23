package com.papelariafrasato.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record RequestPaymentePixDto(
        @Schema(description = "UUID user id", example = "adaw331-asdaf31-242fr2-qd31fe33")
        String userId,
        @Schema(description = "UUID order id", example = "asdaf31-adaw331-qd31fe33-242fr2")
        String orderId) {
}
