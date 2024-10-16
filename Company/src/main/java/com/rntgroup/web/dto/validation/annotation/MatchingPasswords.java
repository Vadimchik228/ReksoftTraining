package com.rntgroup.web.dto.validation.annotation;

import com.rntgroup.web.dto.validation.validator.MatchingPasswordsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MatchingPasswordsValidator.class)
public @interface MatchingPasswords {

    String message() default "Passwords don't match.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
