package com.papelariafrasato.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record RequestAddDiscountProductDto(
        @Schema(description = "product ID", example = "adaw331-asdaf31-242fr2-qd31fe33")
        String productId,
        @Schema(description = "Discount on percent(%)", example = "50%")
        int discount,
        @Schema(description = "The end of the product discount", example = "12/03")
        LocalDate discountEnd
){}
