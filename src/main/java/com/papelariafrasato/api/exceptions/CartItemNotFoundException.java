package com.papelariafrasato.api.exceptions;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(String cartItemId) {
        super("Cart Item on ID: " + cartItemId + "not found");
    }
}
