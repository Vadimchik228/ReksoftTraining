package com.rntgroup.web.dto.validation.annotation;

import com.rntgroup.web.dto.validation.validator.CorrectPhoneNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CorrectPhoneNumberValidator.class)
public @interface CorrectPhoneNumber {
    String message() default "Phone number must be valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
