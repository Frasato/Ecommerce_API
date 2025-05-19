package com.papelariafrasato.api.exceptions;

public class InvalidPriceException extends ProductException {
    public InvalidPriceException() {
        super("O preço do produto não pode ser negativo");
    }
} 