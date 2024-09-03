package com.rntgroup.web.dto.validation.validator;

import com.rntgroup.web.dto.validation.annotation.CorrectPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class CorrectPasswordValidator implements ConstraintValidator<CorrectPassword, String> {
    private static final String REGEX = "^(?=.*[0-9])(?=.*[a-zA-Zа-яА-Я])(?=.*[!\"№%*@?$&]).{6,}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return true;
        }

        String trimPassword = password.trim();
        if (trimPassword.length() < 6) {
            return false;
        }

        return Pattern.compile(REGEX).matcher(trimPassword).matches();
    }
}
