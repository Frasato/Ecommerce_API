package com.papelariafrasato.api.exceptions;

public class ProductNotFoundException extends ProductException {
    public ProductNotFoundException(String productId) {
        super("Produto não encontrado com ID: " + productId);
    }
} 