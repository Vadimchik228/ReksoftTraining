package com.rntgroup.web.dto.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rntgroup.database.entity.Sex;
import com.rntgroup.web.dto.validation.annotation.CorrectName;
import com.rntgroup.web.dto.validation.annotation.CorrectPhoneNumber;
import com.rntgroup.web.dto.validation.annotation.DismissalDateAfterEmploymentDate;
import com.rntgroup.web.dto.validation.annotation.EmploymentDateAfterBirthDate;
import com.rntgroup.web.dto.validation.group.OnCreate;
import com.rntgroup.web.dto.validation.group.OnUpdate;
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

    @Null(
            message = "Id must be null.",
            groups = OnCreate.class
    )
    @NotNull(
            message = "Id must be not null.",
            groups = OnUpdate.class
    )
    private Long id;

    @NotNull(
            message = "Last name must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Size(
            message = "Last name must consist of no more than 35 characters.",
            max = 35
    )
    @CorrectName(
            message = "Last name must be valid"
    )
    private String lastName;

    @NotNull(
            message = "First name must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Size(
            message = "First name must consist of no more than 35 characters.",
            max = 35
    )
    @CorrectName(
            message = "First name must be valid"
    )
    private String firstName;

    @Size(
            message = "Patronymic must consist of no more than 35 characters.",
            max = 35
    )
    @CorrectName(
            message = "Patronymic must be valid"
    )
    private String patronymic;

    @NotNull(
            message = "Sex must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private Sex sex;

    @NotNull(
            message = "Birth date must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Past(
            message = "Birth date must be in the past."
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotNull(
            message = "Phone number must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @CorrectPhoneNumber
    private String phoneNumber;

    @NotNull(
            message = "Employment date must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate employmentDate;

    @Null(
            message = "Dismissal date date must be null.",
            groups = OnCreate.class
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dismissalDate;

    @NotNull(
            message = "Position Id must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private Integer positionId;

    @NotNull(
            message = "Salary must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @DecimalMin(
            value = "0",
            message = "Salary must be not less than 0."
    )
    @DecimalMax(
            value = "100000000",
            inclusive = false,
            message = "Salary must be less than 100 million."
    )
    private BigDecimal salary;

    @NotNull(
            message = "Director flag must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private Boolean isDirector;

    @NotNull(
            message = "Department Id must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private Integer departmentId;

}
