package com.rntgroup.web.controller;

import com.rntgroup.service.EmployeeService;
import com.rntgroup.web.dto.employee.EmployeeDto;
import com.rntgroup.web.dto.response.PageResponse;
import com.rntgroup.web.dto.validation.group.OnCreate;
import com.rntgroup.web.dto.validation.group.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Produces;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Validated
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Employee Controller", description = "Employee API")
public class EmployeeController {

    private final EmployeeService service;

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by id")
    public EmployeeDto getById(@PathVariable final Long id) {
        return service.getById(id);
    }

    @GetMapping("/{departmentId}/director")
    @Operation(summary = "Get director of department by departmentId")
    public Optional<EmployeeDto> getDirectorByDepartmentId(
            @PathVariable final Integer departmentId) {
        return service.getDirectorByDepartmentId(departmentId);
    }

    @GetMapping("/{departmentId}/employees")
    @Operation(summary = "Get all employees of department by departmentId")
    public PageResponse<EmployeeDto> getAllByDepartmentId(
            @PathVariable final Integer departmentId, final Pageable pageable) {
        var page = service.getAllByDepartmentId(departmentId, pageable);
        return PageResponse.of(page);
    }

    @PostMapping
    @Operation(summary = "Create employee")
    @PreAuthorize("@cse.hasAdminRights()")
    public EmployeeDto create(
            @Validated(OnCreate.class) @RequestBody EmployeeDto dto) {
        return service.create(dto);
    }

    @PutMapping
    @Operation(summary = "Update employee")
    @PreAuthorize("@cse.hasAdminRights()")
    public EmployeeDto update(
            @Validated(OnUpdate.class) @RequestBody final EmployeeDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee by id")
    @PreAuthorize("@cse.hasAdminRights()")
    public void delete(@PathVariable final Long id) {
        service.delete(id);
    }

    @GetMapping("/{departmentId}/count")
    @Operation(summary = "Get number of department's employees by departmentId")
    public Long getCountOfAllByDepartmentId(
            @PathVariable final Integer departmentId) {
        return service.countAllByDepartmentId(departmentId);
    }

    @GetMapping("/{departmentId}/salaryFund")
    @Operation(summary = "Get salary fund by departmentId")
    public BigDecimal getSalaryFundByDepartmentId(
            @PathVariable final Integer departmentId) {
        return service.sumSalariesByDepartmentId(departmentId);
    }

}
