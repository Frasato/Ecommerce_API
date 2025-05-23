package com.papelariafrasato.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record RequestProductDto(
        @Schema(description = "MultipartForm Image", example = "jnnasndajsndajnfpajndfjasnjasdasda")
        String image,
        @Schema(description = "Name of the product", example = "Chocolate Bar")
        String name,
        @Schema(description = "Description of the product", example = "Chocolate 100% pure with milk 90 pounds")
        String description,
        @Schema(description = "Price of product", example = "29,90")
        Integer price,
        @Schema(description = "Category of the product", example = "Candy")
        String category
){}
