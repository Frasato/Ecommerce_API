package com.papelariafrasato.api.exceptions;

public class InvalidOrderStatusException extends OrderException {
    public InvalidOrderStatusException(String status) {
        super("Status de pedido inválido: " + status + ". Status válidos são: PENDING, PAID, DELIVERY, FINISH");
    }
} 