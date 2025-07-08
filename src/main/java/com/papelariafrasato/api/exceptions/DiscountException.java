package com.papelariafrasato.api.exceptions;

public class DiscountException extends RuntimeException {
    public DiscountException(int discount) {
        super("Can't put a discount: " + discount +" on a product already have discount");
    }
}
