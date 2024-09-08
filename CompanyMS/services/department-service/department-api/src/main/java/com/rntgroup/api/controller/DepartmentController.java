package com.rntgroup.api.controller;

import com.rntgroup.api.dto.DepartmentDto;
import com.rntgroup.api.dto.PageResponse;
import com.rntgroup.api.dto.validation.group.OnCreate;
import com.rntgroup.api.dto.validation.group.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Produces;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/api/v1/departments")
@Validated
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Department Controller", description = "Department API")
public interface DepartmentController {

    @GetMapping("/{id}")
    @Operation(summary = "Get department by id")
    DepartmentDto getById(@PathVariable Integer id);

    @GetMapping("/name/{name}")
    @Operation(summary = "Get department by name")
    Optional<DepartmentDto> getByName(@PathVariable String name);

    @GetMapping
    @Operation(summary = "Get all departments")
    PageResponse<DepartmentDto> getAll(Pageable pageable);

    @PostMapping
    @Operation(summary = "Create department")
    DepartmentDto create(@Validated(OnCreate.class) @RequestBody DepartmentDto dto);

    @PutMapping
    @Operation(summary = "Update department")
    DepartmentDto update(@Validated(OnUpdate.class) @RequestBody DepartmentDto dto);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete department by id")
    void delete(@PathVariable Integer id);

}
