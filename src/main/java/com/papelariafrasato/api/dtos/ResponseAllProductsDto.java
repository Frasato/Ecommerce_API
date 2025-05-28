package com.papelariafrasato.api.dtos;

import com.papelariafrasato.api.models.Product;

import java.util.List;

public record ResponseAllProductsDto(
        List<Product> products
){}
