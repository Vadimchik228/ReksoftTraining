package com.rntgroup.impl.controller;

import com.rntgroup.api.controller.DepartmentController;
import com.rntgroup.api.dto.DepartmentDto;
import com.rntgroup.api.dto.PageResponse;
import com.rntgroup.impl.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class DepartmentControllerImpl implements DepartmentController {

    private final DepartmentService service;

    @Override
    public DepartmentDto getById(final Integer id) {
        return service.getById(id);
    }

    @Override
    public Optional<DepartmentDto> getByName(final String name) {
        return service.getByName(name);
    }

    @Override
    public PageResponse<DepartmentDto> getAll(final Pageable pageable) {
        var page = service.getAll(pageable);
        return PageResponse.of(page);
    }

    @Override
    public DepartmentDto create(DepartmentDto dto) {
        return service.create(dto);
    }

    @Override
    public DepartmentDto update(final DepartmentDto dto) {
        return service.update(dto);
    }

    @Override
    public void delete(final Integer id) {
        service.delete(id);
    }

}
