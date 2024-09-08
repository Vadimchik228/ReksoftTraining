package com.rntroup.api.dto.validation.validator;

import com.rntroup.api.dto.EmployeeDto;
import com.rntroup.api.dto.validation.annotation.DismissalDateAfterEmploymentDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class DismissalDateValidator
        implements ConstraintValidator<DismissalDateAfterEmploymentDate, EmployeeDto> {

    @Override
    public boolean isValid(final EmployeeDto employeeDto,
                           final ConstraintValidatorContext context) {

        if (employeeDto.getDismissalDate() != null
            && employeeDto.getEmploymentDate() != null) {

            if (!employeeDto.getDismissalDate()
                    .isAfter(employeeDto.getEmploymentDate())) {

                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode("dismissalDate")
                        .addConstraintViolation();

                return false;
            }
        }

        return true;
    }
}
