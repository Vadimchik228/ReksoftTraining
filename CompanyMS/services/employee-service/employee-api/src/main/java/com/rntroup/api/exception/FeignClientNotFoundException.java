package com.rntroup.api.exception;

public class FeignClientNotFoundException extends RuntimeException {
    public FeignClientNotFoundException(final String message) {
        super(message);
    }
}
