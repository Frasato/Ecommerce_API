package com.papelariafrasato.api.exceptions;

public class OrderNotFoundException extends OrderException {
    public OrderNotFoundException(String orderId) {
        super("Pedido não encontrado com ID: " + orderId);
    }
} 