package com.rntgroup.web.controller;

import com.rntgroup.service.PositionService;
import com.rntgroup.web.dto.PageResponse;
import com.rntgroup.web.dto.PositionDto;
import com.rntgroup.web.dto.validation.OnCreate;
import com.rntgroup.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Produces;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/positions")
@RequiredArgsConstructor
@Validated
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Position Controller", description = "Position API")
public class PositionController {

    private final PositionService service;

    @GetMapping("/{id}")
    @Operation(summary = "Get position by id")
    public PositionDto getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping
    @Operation(summary = "Get all positions")
    public PageResponse<PositionDto> getAll(Pageable pageable) {
        var page = service.getAll(pageable);
        return PageResponse.of(page);
    }

    @PostMapping
    @Operation(summary = "Create position")
    public PositionDto create(
            @Validated(OnCreate.class) @RequestBody PositionDto dto) {
        return service.create(dto);
    }

    @PutMapping
    @Operation(summary = "Update position")
    public PositionDto update(
            @Validated(OnUpdate.class) @RequestBody PositionDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete position by id")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

}