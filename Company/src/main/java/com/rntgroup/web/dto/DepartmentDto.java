package com.rntgroup.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rntgroup.web.dto.validation.group.OnCreate;
import com.rntgroup.web.dto.validation.group.OnUpdate;
import com.rntgroup.web.dto.validation.annotation.CorrectParentDepartmentId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Department DTO")
@CorrectParentDepartmentId(
        groups = {OnCreate.class, OnUpdate.class}
)
public class DepartmentDto {

    @Null(
            message = "Id must be null.",
            groups = OnCreate.class
    )
    @NotNull(
            message = "Id must be not null.",
            groups = OnUpdate.class
    )
    private Integer id;

    @NotNull(
            message = "Name must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String name;

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

    private Integer parentDepartmentId;

}
