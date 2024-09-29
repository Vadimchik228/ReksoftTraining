package com.rntgroup.api.unit.controller;

import com.rntroup.api.controller.ControllerAdvice;
import com.rntroup.api.exception.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControllerAdviceTest {

    @InjectMocks
    private ControllerAdvice controllerAdvice;

    @Test
    void handleResourceNotFound() {
        var exception = new ResourceNotFoundException(
                "Resource not found."
        );
        var expectedExceptionBody = new ExceptionBody(exception.getMessage());

        var result = controllerAdvice.handleResourceNotFound(exception);

        assertThat(result).isEqualTo(expectedExceptionBody);
    }

    @Test
    void handleFeignClientNotFound() {
        var exception = new FeignClientNotFoundException(
                "Feign client not found."
        );
        var expectedExceptionBody = new ExceptionBody(exception.getMessage());

        var result = controllerAdvice.handleFeignClientNotFound(exception);

        assertThat(result).isEqualTo(expectedExceptionBody);
    }

    @Test
    void handleInvalidData() {
        var exception = new InvalidDataException(
                "Invalid data."
        );
        var expectedExceptionBody = new ExceptionBody(exception.getMessage());

        var result = controllerAdvice.handleInvalidData(exception);

        assertThat(result).isEqualTo(expectedExceptionBody);
    }

    @Test
    void handleInvalidDeletion() {
        var exception = new InvalidDeletionException(
                "Invalid deletion."
        );
        var expectedExceptionBody = new ExceptionBody(exception.getMessage());

        var result = controllerAdvice.handleInvalidDeletion(exception);

        assertThat(result).isEqualTo(expectedExceptionBody);
    }

    @Test
    void handleMethodArgumentNotValid() {
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError(
                        "departmentDto",
                        "name",
                        "Name must be not null."
                )
        );
        fieldErrors.add(new FieldError(
                        "departmentDto",
                        "parentDepartmentId",
                        "Parent department id cannot be the same as department id."
                )
        );

        var bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        var exception = new MethodArgumentNotValidException(null, bindingResult);

        Map<String, String> expectedErrors = Map.of(
                "name",
                "Name must be not null.",

                "parentDepartmentId",
                "Parent department id cannot be the same as department id."
        );

        var result = controllerAdvice.handleMethodArgumentNotValid(exception);

        assertThat(result.getMessage()).isEqualTo("Validation failed.");
        assertThat(result.getErrors()).isEqualTo(expectedErrors);
    }

    @Test
    void handleConstraintViolation_shouldReturnBadRequestExceptionBodyWithErrors() {
        ConstraintViolation<?> violation1 = mock(
                ConstraintViolation.class,
                withSettings().defaultAnswer(RETURNS_SMART_NULLS)
        );
        var path1 = mock(Path.class);
        when(path1.toString())
                .thenReturn("name");
        when(violation1.getPropertyPath())
                .thenReturn(path1);
        when(violation1.getMessage())
                .thenReturn("Name must be not null.");

        ConstraintViolation<?> violation2 = mock(
                ConstraintViolation.class,
                withSettings().defaultAnswer(RETURNS_SMART_NULLS)
        );
        var path2 = mock(Path.class);
        when(path2.toString())
                .thenReturn("parentDepartmentId");
        when(violation2.getPropertyPath())
                .thenReturn(path2);
        when(violation2.getMessage())
                .thenReturn("Parent department id cannot be the same as department id.");

        var exception = new ConstraintViolationException(
                Set.of(violation1, violation2)
        );

        Map<String, String> expectedErrors = Map.of(
                "name",
                "Name must be not null.",

                "parentDepartmentId",
                "Parent department id cannot be the same as department id."
        );

        var result = controllerAdvice.handleConstraintViolation(exception);

        assertThat(result.getMessage())
                .isEqualTo("Validation failed.");
        assertThat(result.getErrors())
                .isEqualTo(expectedErrors);
    }

    @Test
    void handleMessageNotReadable() {
        var exception = new HttpMessageNotReadableException(
                "Error reading request body."
        );

        Map<String, String> expectedErrors = Map.of(
                "body",
                "Error reading request body."
        );

        var result = controllerAdvice.handleMessageNotReadable(exception);

        assertThat(result.getMessage()).isEqualTo("Validation failed.");
        assertThat(result.getErrors()).isEqualTo(expectedErrors);
    }

    @Test
    void handleJsonProcessingException() {
        var exception = new DtoDeserializationException(
                "Error while deserializing JSON to DTO."
        );
        var expectedExceptionBody = new ExceptionBody(exception.getMessage());

        var result = controllerAdvice.handleJsonProcessingException(exception);

        assertThat(result).isEqualTo(expectedExceptionBody);
    }

    @Test
    void handleException() {
        var exception = new Exception("Internal error.");
        var expectedExceptionBody = new ExceptionBody("Internal error.");

        var result = controllerAdvice.handleException(exception);

        assertThat(result).isEqualTo(expectedExceptionBody);
    }
}
