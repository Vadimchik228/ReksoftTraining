package com.rntroup.api.dto;

import com.rntroup.api.dto.validation.group.OnCreate;
import com.rntroup.api.dto.validation.group.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Position DTO")
public class PositionDto {

    @Schema(
            description = "Position id",
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
    private Integer id;

    @Schema(
            description = "Position name",
            example = "Финансовый директор (CFO)"
    )
    @NotNull(
            message = "Name must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String name;

}
