package com.rntroup.api.dto.validation.validator;

import com.rntroup.api.dto.validation.annotation.CorrectName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class CorrectNameValidator implements ConstraintValidator<CorrectName, String> {

    private static final String REGEX = "^[А-ЯЁA-Z][а-яёa-zА-ЯЁA-Z\\-]*$";

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null) {
            return true;
        }

        String trimName = name.trim();
        return trimName.isEmpty() || Pattern.matches(REGEX, trimName);
    }
}
