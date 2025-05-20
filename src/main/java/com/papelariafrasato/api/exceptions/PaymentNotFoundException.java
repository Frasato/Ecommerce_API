package com.papelariafrasato.api.exceptions;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String paymentId) {
        super(paymentId);
    }
}
