package com.rntgroup.web.dto.validation.annotation;

import com.rntgroup.web.dto.validation.validator.CorrectPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CorrectPasswordValidator.class)
public @interface CorrectPassword {
    String message() default "The password must be at least 6 characters long " +
                             "and contain at least one letter, one number " +
                             "and character from this list: {!,№,%,*,@,?,$,&}.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
