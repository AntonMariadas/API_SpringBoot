package com.projet.springapi.exception;

public class ExistingEmailException extends Exception {

    public ExistingEmailException(String message) {
        super(message);
    }
}
