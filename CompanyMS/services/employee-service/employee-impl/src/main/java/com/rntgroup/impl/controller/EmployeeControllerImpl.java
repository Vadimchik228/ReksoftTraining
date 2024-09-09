package com.rntgroup.impl.controller;

import com.rntgroup.impl.service.EmployeeService;
import com.rntroup.api.controller.EmployeeController;
import com.rntroup.api.dto.EmployeeDto;
import com.rntroup.api.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class EmployeeControllerImpl implements EmployeeController {

    private final EmployeeService service;

    @Override
    public EmployeeDto getById(final Long id) {
        return service.getById(id);
    }

    @Override
    public Optional<EmployeeDto> getDirectorByDepartmentId(
            final Integer departmentId) {
        return service.getDirectorByDepartmentId(departmentId);
    }

    @Override
    public PageResponse<EmployeeDto> getAllByDepartmentId(
            final Integer departmentId, final Pageable pageable) {
        var page = service.getAllByDepartmentId(departmentId, pageable);
        return PageResponse.of(page);
    }

    @Override
    public EmployeeDto create(EmployeeDto dto) {
        return service.create(dto);
    }

    @Override
    public EmployeeDto update(final EmployeeDto dto) {
        return service.update(dto);
    }

    @Override
    public void delete(final Long id) {
        service.delete(id);
    }

    @Override
    public Long getCountOfAllByDepartmentId(final Integer departmentId) {
        return service.countAllByDepartmentId(departmentId);
    }

    @Override
    public BigDecimal getSalaryFundByDepartmentId(final Integer departmentId) {
        return service.sumSalariesByDepartmentId(departmentId);
    }

}