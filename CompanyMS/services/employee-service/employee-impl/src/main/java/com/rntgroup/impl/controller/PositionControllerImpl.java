package com.rntgroup.impl.controller;

import com.rntgroup.impl.service.PositionService;
import com.rntroup.api.controller.PositionController;
import com.rntroup.api.dto.PageResponse;
import com.rntroup.api.dto.PositionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PositionControllerImpl implements PositionController {

    private final PositionService service;

    @Override
    public PositionDto getById(final Integer id) {
        return service.getById(id);
    }

    @Override
    public PageResponse<PositionDto> getAll(final Pageable pageable) {
        var page = service.getAll(pageable);
        return PageResponse.of(page);
    }

    @Override
    public PositionDto create(PositionDto dto) {
        return service.create(dto);
    }

    @Override
    public PositionDto update(final PositionDto dto) {
        return service.update(dto);
    }

    @Override
    public void delete(final Integer id) {
        service.delete(id);
    }

}
