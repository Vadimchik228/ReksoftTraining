package com.rntgroup.web.dto.validation.validator;

import com.rntgroup.web.dto.user.UserDto;
import com.rntgroup.web.dto.validation.annotation.MatchingPasswords;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MatchingPasswordsValidator
        implements ConstraintValidator<MatchingPasswords, UserDto> {

    @Override
    public boolean isValid(UserDto userDto, ConstraintValidatorContext context) {

        if (userDto.getPassword() != null && userDto.getPasswordConfirmation() != null) {
            boolean matched = userDto.getPassword().equals(userDto.getPasswordConfirmation());

            if (!matched) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode("passwordConfirmation")
                        .addConstraintViolation();
            }

            return matched;
        }

        return true;
    }
}