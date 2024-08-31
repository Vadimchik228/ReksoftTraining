package com.rntgroup.web.dto.validation.annotation;

import com.rntgroup.web.dto.validation.validator.EmploymentDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmploymentDateValidator.class)
public @interface EmploymentDateAfterBirthDate {
    String message() default "The date of employment must be 18 years after the date of birth.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}