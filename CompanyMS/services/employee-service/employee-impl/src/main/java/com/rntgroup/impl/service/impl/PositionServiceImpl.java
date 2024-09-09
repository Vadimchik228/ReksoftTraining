package com.rntgroup.impl.service.impl;

import com.rntgroup.impl.mapper.PositionMapper;
import com.rntgroup.impl.repository.PositionRepository;
import com.rntgroup.impl.service.PositionService;
import com.rntroup.api.dto.PositionDto;
import com.rntroup.api.exception.InvalidDataException;
import com.rntroup.api.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;

@Service
@RequiredArgsConstructor
@Transactional
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;
    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public PositionDto getById(final Integer id) {
        return positionRepository.findById(id)
                .map(positionMapper::toDto)
                .orElseThrow(() -> new ResolutionException(
                                "Couldn't find position with id " + id + "."
                        )
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PositionDto> getAll(final Pageable pageable) {
        return positionRepository.findAllBy(pageable)
                .map(positionMapper::toDto);
    }

    @Override
    public PositionDto update(final PositionDto dto) {
        checkIfPositionIdExists(dto.getId());

        try {
            positionRepository.saveAndFlush(positionMapper.toEntity(dto));
            return dto;
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidDataException(
                    "There is already position with name " + dto.getName() + "."
            );
        }
    }

    @Override
    public PositionDto create(PositionDto dto) {
        try {
            var entity = positionRepository.saveAndFlush(positionMapper.toEntity(dto));
            dto.setId(entity.getId());
            return dto;
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidDataException(
                    "There is already position with name " + dto.getName() + "."
            );
        }
    }

    @Override
    public void delete(final Integer id) {
        checkIfPositionIdExists(id);

        try {
            positionRepository.deleteById(id);
            entityManager.flush();
        } catch (ConstraintViolationException ex) {
            throw new InvalidDataException(
                    "Couldn't delete position with id " + id + "." +
                    " There are dependent employees in the DB."
            );
        }
    }

    private void checkIfPositionIdExists(final Integer positionId) {
        if (!positionRepository.existsById(positionId)) {
            throw new ResourceNotFoundException(
                    "Couldn't find position with id " + positionId + "."
            );
        }
    }

}
