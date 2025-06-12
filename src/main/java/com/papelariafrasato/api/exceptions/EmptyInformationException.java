package com.papelariafrasato.api.exceptions;

public class EmptyInformationException extends RuntimeException {
    public EmptyInformationException() {
        super("There are invalid or empty information");
    }
}
