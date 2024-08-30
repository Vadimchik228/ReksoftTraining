package com.rntgroup.web.dto.validation.validator;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.rntgroup.web.dto.validation.annotation.CorrectPhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class CorrectPhoneNumberValidator
        implements ConstraintValidator<CorrectPhoneNumber, String> {

    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {

        if (phoneNumber == null) {
            return false;
        }
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(phoneNumber, "RU");
            return phoneUtil.isValidNumber(number);
        } catch (NumberParseException e) {
            return false;
        }

    }
}
