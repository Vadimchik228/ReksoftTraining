package com.rntgroup.exception;

public class InvalidDeletionException extends RuntimeException {
    public InvalidDeletionException(final String message) {
        super(message);
    }
}
