package com.rntgroup.api.unit.validator;

import com.rntroup.api.dto.EmployeeDto;
import com.rntroup.api.dto.validation.validator.DismissalDateValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DismissalDateValidatorTest {

    private final DismissalDateValidator validator = new DismissalDateValidator();

    @Test
    void isValid_whenDismissalDateIsNull() {
        var employmentDate = LocalDate.now();

        var employeeDto = getEmployeeDto(
                employmentDate,
                null
        );

        var context = mock(ConstraintValidatorContext.class);
        assertThat(validator.isValid(employeeDto, context)).isTrue();
    }

    @Test
    void isValid_whenDismissalDateAfterEmploymentDate() {
        var employmentDate = LocalDate.of(2023, 1, 1);
        var dismissalDate = LocalDate.now();

        var employeeDto = getEmployeeDto(
                employmentDate,
                dismissalDate
        );

        var context = mock(ConstraintValidatorContext.class);
        assertThat(validator.isValid(employeeDto, context)).isTrue();
    }

    @Test
    void isValid_whenDismissalDateBeforeEmploymentDate() {
        assertInvalid(
                LocalDate.now(),
                LocalDate.of(2023, 1, 1)
        );
    }

    @Test
    void isValid_whenDismissalDateEqualsEmploymentDate() {
        assertInvalid(
                LocalDate.now(),
                LocalDate.now()
        );
    }

    private void assertInvalid(final LocalDate employmentDate, final LocalDate dismissalDate) {
        var employeeDto = getEmployeeDto(employmentDate, dismissalDate);
        var context = mock(ConstraintValidatorContext.class);
        var builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        var nodeBuilder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);

        when(context.getDefaultConstraintMessageTemplate()).thenReturn("");
        when(context.buildConstraintViolationWithTemplate(anyString()))
                .thenReturn(builder);
        when(builder.addPropertyNode(anyString()))
                .thenReturn(nodeBuilder);
        when(nodeBuilder.addConstraintViolation()).thenReturn(context);

        assertThat(validator.isValid(employeeDto, context)).isFalse();
    }

    private EmployeeDto getEmployeeDto(final LocalDate employmentDate,
                                       final LocalDate dismissalDate) {
        return new EmployeeDto(
                1L,
                "Новая фамилия",
                "Новое имя",
                "Новое отчество",
                "MALE",
                LocalDate.now(),
                "+79507742275",
                employmentDate,
                dismissalDate,
                1,
                BigDecimal.valueOf(100000),
                false,
                1
        );
    }
}
