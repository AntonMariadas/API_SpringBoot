package com.projet.springapi.exception;

public class ExistingApplicationInHistoryException extends Exception {

    public ExistingApplicationInHistoryException(String message) {
        super(message);
    }
}
