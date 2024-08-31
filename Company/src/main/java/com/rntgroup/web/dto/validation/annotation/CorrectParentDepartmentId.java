package com.rntgroup.web.dto.validation.annotation;

import com.rntgroup.web.dto.validation.validator.CorrectParentDepartmentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CorrectParentDepartmentValidator.class)
public @interface CorrectParentDepartmentId {
    String message() default "Parent department id cannot be the same as department id.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}