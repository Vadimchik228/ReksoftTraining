package com.rntgroup.api.unit.validator;

import com.rntgroup.api.dto.DepartmentDto;
import com.rntgroup.api.dto.validation.validator.CorrectParentDepartmentValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CorrectParentDepartmentValidatorTest {

    private final CorrectParentDepartmentValidator validator = new CorrectParentDepartmentValidator();

    @Test
    void isValid_nullParentDepartmentId() {
        var departmentDto = getDepartmentDto();

        var context = mock(ConstraintValidatorContext.class);
        assertThat(validator.isValid(departmentDto, context)).isTrue();
    }

    @Test
    void isValid_differentParentDepartmentId() {
        var departmentDto = getDepartmentDto();
        departmentDto.setParentDepartmentId(2);

        var context = mock(ConstraintValidatorContext.class);
        assertThat(validator.isValid(departmentDto, context)).isTrue();
    }

    @Test
    void isValid_sameParentDepartmentId() {
        var departmentDto = getDepartmentDto();
        departmentDto.setParentDepartmentId(1);

        var context = mock(ConstraintValidatorContext.class);
        var builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        var nodeBuilder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);

        when(context.getDefaultConstraintMessageTemplate()).thenReturn("");
        when(context.buildConstraintViolationWithTemplate(anyString()))
                .thenReturn(builder);
        when(builder.addPropertyNode(anyString()))
                .thenReturn(nodeBuilder);
        when(nodeBuilder.addConstraintViolation()).thenReturn(context);

        assertThat(validator.isValid(departmentDto, context)).isFalse();
    }

    @Test
    void isValid_parentDepartmentIdIsNullButIdIsNotNull() {
        var departmentDto = getDepartmentDto();
        departmentDto.setId(null);

        var context = mock(ConstraintValidatorContext.class);
        assertThat(validator.isValid(departmentDto, context)).isTrue();
    }

    private DepartmentDto getDepartmentDto() {
        var departmentDto = new DepartmentDto();
        departmentDto.setId(1);
        departmentDto.setName("Some name");
        departmentDto.setCreationDate(LocalDate.now());
        departmentDto.setParentDepartmentId(null);
        return departmentDto;
    }

}
