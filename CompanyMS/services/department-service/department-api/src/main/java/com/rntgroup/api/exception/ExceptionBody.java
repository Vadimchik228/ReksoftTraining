package com.rntgroup.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;

import java.util.Map;

@Data
@AllArgsConstructor
@Generated
public class ExceptionBody {

    private String message;
    private Map<String, String> errors;

    public ExceptionBody(final String message) {
        this.message = message;
    }

}
