package com.rntgroup.service;

import com.rntgroup.web.dto.employee.EmployeeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public interface EmployeeService {
    EmployeeDto getById(Long id);

    Optional<EmployeeDto> getDirectorByDepartmentId(Integer departmentId);

    Page<EmployeeDto> getAllByDepartmentId(Integer departmentId, Pageable pageable);

    EmployeeDto update(EmployeeDto dto);

    EmployeeDto create(EmployeeDto dto);

    void delete(Long id);

    Long countAllByDepartmentId(Integer departmentId);

    BigDecimal sumSalariesByDepartmentId(Integer departmentId);
}
