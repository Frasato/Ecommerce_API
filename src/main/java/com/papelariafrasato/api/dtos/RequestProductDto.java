package com.papelariafrasato.api.dtos;

public record RequestProductDto(
        String image,
        String name,
        String description,
        Integer price,
        String category
){}
