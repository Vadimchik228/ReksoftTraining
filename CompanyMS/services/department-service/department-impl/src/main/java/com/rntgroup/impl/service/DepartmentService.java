package com.rntgroup.impl.service;

import com.rntgroup.api.dto.DepartmentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DepartmentService {
    DepartmentDto getById(Integer id);

    Optional<DepartmentDto> getByName(String name);

    Page<DepartmentDto> getAll(Pageable pageable);

    DepartmentDto update(DepartmentDto dto);

    DepartmentDto create(DepartmentDto dto);

    void delete(Integer id);
}
