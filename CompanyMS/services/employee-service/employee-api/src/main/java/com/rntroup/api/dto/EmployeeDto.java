package com.rntroup.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rntroup.api.dto.validation.annotation.CorrectName;
import com.rntroup.api.dto.validation.annotation.CorrectPhoneNumber;
import com.rntroup.api.dto.validation.annotation.DismissalDateAfterEmploymentDate;
import com.rntroup.api.dto.validation.annotation.EmploymentDateAfterBirthDate;
import com.rntroup.api.dto.validation.group.OnCreate;
import com.rntroup.api.dto.validation.group.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Employee DTO")
@EmploymentDateAfterBirthDate(
        groups = {OnCreate.class, OnUpdate.class}
)
@DismissalDateAfterEmploymentDate(
        groups = {OnCreate.class, OnUpdate.class}
)
public class EmployeeDto {

    @Schema(
            description = "Employee id",
            example = "1"
    )
    @Null(
            message = "Id must be null.",
            groups = OnCreate.class
    )
    @NotNull(
            message = "Id must be not null.",
            groups = OnUpdate.class
    )
    private Long id;

    @Schema(
            description = "Employee's lastname",
            example = "Щебетовский"
    )
    @NotNull(
            message = "Last name must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Size(
            message = "Last name must consist of no more than 35 characters.",
            max = 35,
            groups = {OnCreate.class, OnUpdate.class}
    )
    @CorrectName(
            message = "Last name must be valid",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String lastName;

    @Schema(
            description = "Employee's firstname",
            example = "Вадим"
    )
    @NotNull(
            message = "First name must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Size(
            message = "First name must consist of no more than 35 characters.",
            max = 35,
            groups = {OnCreate.class, OnUpdate.class}
    )
    @CorrectName(
            message = "First name must be valid",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String firstName;

    @Schema(
            description = "Employee's patronymic",
            example = "Александрович"
    )
    @Size(
            message = "Patronymic must consist of no more than 35 characters.",
            max = 35,
            groups = {OnCreate.class, OnUpdate.class}
    )
    @CorrectName(
            message = "Patronymic must be valid",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String patronymic;

    @Schema(
            description = "Employee's sex",
            example = "MALE"
    )
    @NotNull(
            message = "Sex must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Pattern(
            regexp = "^(MALE|FEMALE)$",
            message = "Sex must be either MALE or FEMALE.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String sex;

    @Schema(
            description = "Employee's birth date",
            example = "2003-04-11"
    )
    @NotNull(
            message = "Birth date must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Past(
            message = "Birth date must be in the past.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Schema(
            description = "Employee's phone number",
            example = "+7 (950) 774 22 27"
    )
    @NotNull(
            message = "Phone number must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @CorrectPhoneNumber(
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String phoneNumber;

    @Schema(
            description = "Employment date",
            example = "2024-10-10"
    )
    @NotNull(
            message = "Employment date must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate employmentDate;

    @Schema(
            description = "Dismissal date",
            example = "2025-10-10"
    )
    @Null(
            message = "Dismissal date date must be null.",
            groups = OnCreate.class
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dismissalDate;

    @Schema(
            description = "Id of the position held by the employee",
            example = "1"
    )
    @NotNull(
            message = "Position Id must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private Integer positionId;

    @Schema(
            description = "Employee's salary",
            example = "100000.00"
    )
    @NotNull(
            message = "Salary must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @DecimalMin(
            value = "0",
            message = "Salary must be not less than 0.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @DecimalMax(
            value = "100000000",
            inclusive = false,
            message = "Salary must be less than 100 million.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private BigDecimal salary;

    @Schema(
            description = "Flag whether the employee is a director",
            example = "false"
    )
    @NotNull(
            message = "Director flag must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private Boolean isDirector;

    @Schema(
            description = "Id of the department where the employee works",
            example = "1"
    )
    @NotNull(
            message = "Department Id must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private Integer departmentId;

}
