package com.papelariafrasato.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record RequestAddDiscountProductDto(
        @Schema(description = "UUID user id", example = "adaw331-asdaf31-242fr2-qd31fe33")
        String productId,
        @Schema(description = "UUID product id", example = "adaw331-ffke59-242fr2-zxxl990")
        int discount
){}
