package com.rntroup.api.controller;

import com.rntroup.api.dto.EmployeeDto;
import com.rntroup.api.dto.PageResponse;
import com.rntroup.api.dto.validation.group.OnCreate;
import com.rntroup.api.dto.validation.group.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Produces;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RequestMapping("/api/v1/employees")
@Validated
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Employee Controller", description = "Employee API")
public interface EmployeeController {

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by id")
    EmployeeDto getById(@PathVariable Long id);

    @GetMapping("/{departmentId}/director")
    @Operation(summary = "Get director of department by departmentId")
    Optional<EmployeeDto> getDirectorByDepartmentId(@PathVariable Integer departmentId);

    @GetMapping("/{departmentId}/employees")
    @Operation(summary = "Get all employees of department by departmentId")
    PageResponse<EmployeeDto> getAllByDepartmentId(
            @PathVariable Integer departmentId, Pageable pageable);

    @PostMapping
    @Operation(summary = "Create employee")
    EmployeeDto create(
            @Validated(OnCreate.class) @RequestBody EmployeeDto dto);

    @PutMapping
    @Operation(summary = "Update employee")
    EmployeeDto update(@Validated(OnUpdate.class) @RequestBody EmployeeDto dto);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee by id")
    void delete(@PathVariable Long id);

    @GetMapping("/{departmentId}/count")
    @Operation(summary = "Get number of department's employees by departmentId")
    Long getCountOfAllByDepartmentId(@PathVariable Integer departmentId);

    @GetMapping("/{departmentId}/salaryFund")
    @Operation(summary = "Get salary fund by departmentId")
    BigDecimal getSalaryFundByDepartmentId(@PathVariable Integer departmentId);

}
