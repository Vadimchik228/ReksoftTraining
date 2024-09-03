package com.rntgroup.web.dto.validation.validator;

import com.rntgroup.web.dto.department.DepartmentDto;
import com.rntgroup.web.dto.validation.annotation.CorrectParentDepartmentId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class CorrectParentDepartmentValidator
        implements ConstraintValidator<CorrectParentDepartmentId, DepartmentDto> {

    @Override
    public boolean isValid(DepartmentDto departmentDto, ConstraintValidatorContext context) {
        if (departmentDto.getParentDepartmentId() == null) {
            return true;
        }

        if (departmentDto.getId().equals(departmentDto.getParentDepartmentId())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("parentDepartmentId")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
