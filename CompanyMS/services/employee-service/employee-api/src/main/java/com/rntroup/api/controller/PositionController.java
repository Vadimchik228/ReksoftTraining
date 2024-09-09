package com.rntroup.api.controller;

import com.rntroup.api.dto.PageResponse;
import com.rntroup.api.dto.PositionDto;
import com.rntroup.api.dto.validation.group.OnCreate;
import com.rntroup.api.dto.validation.group.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Produces;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/positions")
@Validated
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Position Controller", description = "Position API")
public interface PositionController {

    @GetMapping("/{id}")
    @Operation(summary = "Get position by id")
    PositionDto getById(@PathVariable Integer id);

    @GetMapping
    @Operation(summary = "Get all positions")
    PageResponse<PositionDto> getAll(Pageable pageable);

    @PostMapping
    @Operation(summary = "Create position")
    PositionDto create(@Validated(OnCreate.class) @RequestBody PositionDto dto);

    @PutMapping
    @Operation(summary = "Update position")
    PositionDto update(@Validated(OnUpdate.class) @RequestBody PositionDto dto);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete position by id")
    void delete(@PathVariable Integer id);

}
