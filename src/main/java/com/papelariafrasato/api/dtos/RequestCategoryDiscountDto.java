package com.papelariafrasato.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record RequestCategoryDiscountDto(
        @Schema(description = "Product Category", example = "Dermatologic")
        String category,
        @Schema(description = "% of discount", example = "20")
        int discount
){}
