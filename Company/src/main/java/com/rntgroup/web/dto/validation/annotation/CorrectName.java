package com.rntgroup.web.dto.validation.annotation;

import com.rntgroup.web.dto.validation.validator.CorrectNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CorrectNameValidator.class)
public @interface CorrectName {
    String message() default "Name must start with a capital letter and contain only letters and hyphens.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
