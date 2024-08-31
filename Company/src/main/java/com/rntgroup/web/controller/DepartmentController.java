package com.rntgroup.web.controller;

import com.rntgroup.service.DepartmentService;
import com.rntgroup.web.dto.DepartmentDto;
import com.rntgroup.web.dto.PageResponse;
import com.rntgroup.web.dto.validation.group.OnCreate;
import com.rntgroup.web.dto.validation.group.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Produces;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Validated
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Department Controller", description = "Department API")
public class DepartmentController {

    private final DepartmentService service;

    @GetMapping("/{id}")
    @Operation(summary = "Get department by id")
    public DepartmentDto getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get department by name")
    public Optional<DepartmentDto> getByName(@PathVariable String name) {
        return service.getByName(name);
    }

    @GetMapping
    @Operation(summary = "Get all departments")
    public PageResponse<DepartmentDto> getAll(Pageable pageable) {
        var page = service.getAll(pageable);
        return PageResponse.of(page);
    }

    @PostMapping
    @Operation(summary = "Create department")
    public DepartmentDto create(
            @Validated(OnCreate.class) @RequestBody DepartmentDto dto) {
        return service.create(dto);
    }

    @PutMapping
    @Operation(summary = "Update department")
    public DepartmentDto update(
            @Validated(OnUpdate.class) @RequestBody DepartmentDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete department by id")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

}
