package com.rntroup.api.dto.validation.validator;

import com.rntroup.api.dto.EmployeeDto;
import com.rntroup.api.dto.validation.annotation.EmploymentDateAfterBirthDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.time.Period;

@Component
public class EmploymentDateValidator
        implements ConstraintValidator<EmploymentDateAfterBirthDate, EmployeeDto> {

    @Override
    public boolean isValid(final EmployeeDto employeeDto,
                           final ConstraintValidatorContext context) {

        if (employeeDto.getEmploymentDate() != null && employeeDto.getBirthDate() != null) {
            Period period = Period.between(employeeDto.getBirthDate(),
                    employeeDto.getEmploymentDate());

            if (period.getYears() < 18) {

                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode("employmentDate")
                        .addConstraintViolation();

                return false;
            }
        }

        return true;
    }
}
