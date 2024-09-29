package com.rntgroup.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rntgroup.api.dto.validation.annotation.CorrectParentDepartmentId;
import com.rntgroup.api.dto.validation.group.OnCreate;
import com.rntgroup.api.dto.validation.group.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Generated
@Schema(description = "Department DTO")
@CorrectParentDepartmentId(
        groups = {OnCreate.class, OnUpdate.class}
)
public class DepartmentDto {

    @Schema(
            description = "Department id",
            example = "2"
    )
    @Null(
            message = "Id must be null.",
            groups = OnCreate.class
    )
    @NotNull(
            message = "Id must be not null.",
            groups = OnUpdate.class
    )
    private Integer id;

    @Schema(
            description = "Department name",
            example = "1"
    )
    @NotNull(
            message = "Name must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String name;

    @Schema(
            description = "Date of creation of the department",
            example = "2024-01-01"
    )
    @NotNull(
            message = "Creation date must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Past(
            message = "Creation date must be in the past.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDate;

    @Schema(
            description = "Id of the direct management department",
            example = "1"
    )
    private Integer parentDepartmentId;

}
