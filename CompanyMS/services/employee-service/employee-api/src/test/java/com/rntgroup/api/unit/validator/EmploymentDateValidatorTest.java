package com.rntgroup.api.unit.validator;

import com.rntroup.api.dto.EmployeeDto;
import com.rntroup.api.dto.validation.validator.EmploymentDateValidator;
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
class EmploymentDateValidatorTest {

    private final EmploymentDateValidator validator = new EmploymentDateValidator();

    @Test
    void isValid_whenEmploymentDateAfter18YearsFromBirthDate() {
        var birthDate = LocalDate.of(2005, 1, 1);
        var employmentDate = birthDate.plusYears(20);
        var employeeDto = getEmployeeDto(
                birthDate,
                employmentDate
        );

        var context = mock(ConstraintValidatorContext.class);
        assertThat(validator.isValid(employeeDto, context)).isTrue();
    }

    @Test
    void isValid_whenEmploymentDateAfterLessThan18YearsFromBirthDate() {
        assertInvalid(
                LocalDate.of(2005, 1, 1),
                LocalDate.of(2005, 1, 1).plusYears(15)
        );
    }

    @Test
    void isValid_whenEmploymentDateBeforeBirthDate() {
        assertInvalid(
                LocalDate.of(2005, 1, 1),
                LocalDate.of(2004, 1, 1)
        );
    }

    @Test
    void isValid_whenEmploymentDateEqualsBirthDate() {
        assertInvalid(
                LocalDate.of(2005, 1, 1),
                LocalDate.of(2005, 1, 1)
        );
    }

    private void assertInvalid(final LocalDate birthDate, final LocalDate employmentDate) {
        var employeeDto = getEmployeeDto(birthDate, employmentDate);
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

    private EmployeeDto getEmployeeDto(final LocalDate birthDate,
                                       final LocalDate employmentDate) {
        return new EmployeeDto(
                1L,
                "Новая фамилия",
                "Новое имя",
                "Новое отчество",
                "MALE",
                birthDate,
                "+79507742275",
                employmentDate,
                null,
                1,
                BigDecimal.valueOf(100000),
                false,
                1
        );
    }
}
