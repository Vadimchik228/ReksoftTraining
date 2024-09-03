package com.rntgroup.service;

import com.rntgroup.web.dto.position.PositionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PositionService {
    PositionDto getById(Integer id);

    Page<PositionDto> getAll(Pageable pageable);

    PositionDto update(PositionDto dto);

    PositionDto create(PositionDto dto);

    void delete(Integer id);
}
