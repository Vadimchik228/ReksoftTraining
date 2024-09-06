package com.rntgroup.web.dto.validation.validator;

import com.rntgroup.web.dto.validation.annotation.EmploymentDateAfterBirthDate;
import com.rntgroup.web.dto.employee.EmployeeDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.time.Period;

@Component
public class EmploymentDateValidator
        implements ConstraintValidator<EmploymentDateAfterBirthDate, EmployeeDto> {

    @Override
    public boolean isValid(EmployeeDto employeeDto, ConstraintValidatorContext context) {

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
