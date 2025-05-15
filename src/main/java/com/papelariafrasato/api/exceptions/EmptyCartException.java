package com.papelariafrasato.api.exceptions;

public class EmptyCartException extends OrderException {
    public EmptyCartException() {
        super("Não é possível criar um pedido com carrinho vazio");
    }
} 