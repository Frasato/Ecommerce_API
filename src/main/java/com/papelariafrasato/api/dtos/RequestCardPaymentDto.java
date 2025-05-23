package com.papelariafrasato.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record RequestCardPaymentDto(
        @Schema(description = "UUID user id", example = "adaw331-asdaf31-242fr2-qd31fe33")
        String userId,
        @Schema(description = "UUID order id", example = "adaw331-ffke59-242fr2-zxxl990")
        String orderId,
        @Schema(description = "Numbers of card parcel", example = "5")
        int parcel
) {
}
