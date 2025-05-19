package com.papelariafrasato.api.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("Usuário não encontrado com ID: " + userId);
    }
} 