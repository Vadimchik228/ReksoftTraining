package com.rntgroup.impl.service;

import com.rntroup.api.dto.PositionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PositionService {
    PositionDto getById(Integer id);

    Page<PositionDto> getAll(Pageable pageable);

    PositionDto update(PositionDto dto);

    PositionDto create(PositionDto dto);

    void delete(Integer id);
}
