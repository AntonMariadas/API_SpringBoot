package com.projet.springapi.exception;

public class InvalidEmailException extends Exception {

    public InvalidEmailException(String message) {
        super(message);
    }
}
