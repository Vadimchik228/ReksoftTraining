package com.rntroup.api.exception;

public class InvalidDeletionException extends RuntimeException {
    public InvalidDeletionException(final String message) {
        super(message);
    }
}
