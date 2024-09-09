package com.rntroup.api.dto.validation.annotation;

import com.rntroup.api.dto.validation.validator.DismissalDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DismissalDateValidator.class)
public @interface DismissalDateAfterEmploymentDate {
    String message() default "Dismissal date must be after employment date.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
