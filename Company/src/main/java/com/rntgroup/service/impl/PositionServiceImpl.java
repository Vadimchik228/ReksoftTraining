package com.rntgroup.service.impl;

import com.rntgroup.database.repository.PositionRepository;
import com.rntgroup.exception.InvalidDataException;
import com.rntgroup.exception.InvalidDeletionException;
import com.rntgroup.exception.ResourceNotFoundException;
import com.rntgroup.service.PositionService;
import com.rntgroup.web.dto.position.PositionDto;
import com.rntgroup.web.mapper.PositionMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new ResourceNotFoundException(
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
            throw new InvalidDeletionException(
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
