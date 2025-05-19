package com.papelariafrasato.api.exceptions;

public class CartNotFoundException extends CartException {
    public CartNotFoundException(String userId) {
        super("Carrinho não encontrado para o usuário: " + userId);
    }
} 