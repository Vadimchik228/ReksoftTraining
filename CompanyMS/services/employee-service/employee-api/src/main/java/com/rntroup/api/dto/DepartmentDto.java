package com.rntroup.api.dto;

import java.time.LocalDate;

public record DepartmentDto(
        Integer id,
        String name,
        LocalDate creationDate,
        Integer parentDepartmentId
) {
}