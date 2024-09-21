package com.rntroup.api.exception;

public class DtoDeserializationException extends RuntimeException {
    public DtoDeserializationException(final String message) {
        super(message);
    }
}