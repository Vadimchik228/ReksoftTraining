package com.rntgroup.service.impl;

import com.rntgroup.database.repository.DepartmentRepository;
import com.rntgroup.exception.InvalidDataException;
import com.rntgroup.exception.InvalidDeletionException;
import com.rntgroup.exception.ResourceNotFoundException;
import com.rntgroup.service.DepartmentService;
import com.rntgroup.web.dto.DepartmentDto;
import com.rntgroup.web.mapper.DepartmentMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public DepartmentDto getById(Integer id) {
        return departmentRepository.findById(id)
                .map(departmentMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Couldn't find department with id " + id + "."));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepartmentDto> getByName(String name) {
        return departmentRepository.findByName(name)
                .map(departmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentDto> getAll(Pageable pageable) {
        return departmentRepository.findAllByDepartmentHierarchy(pageable)
                .map(departmentMapper::toDto);
    }

    @Override
    public DepartmentDto update(DepartmentDto dto) {
        checkIfTheDepartmentIdExists(dto.getId());

        var parentId = dto.getParentDepartmentId();
        if (parentId != null) {
            if (!departmentRepository.existsById(parentId)) {
                throw new ResourceNotFoundException(
                        "Couldn't find parent department with id " + parentId + ".");
            }
        }

        try {
            var entity = departmentMapper.toEntity(dto);
            if (parentId != null) {
                var parentEntity = departmentRepository.findById(parentId);
                entity.setParentDepartment(parentEntity.get());
            }
            departmentRepository.saveAndFlush(entity);
            return dto;
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidDataException(
                    "There is already department with name " + dto.getName() + ".");
        }
    }

    @Override
    public DepartmentDto create(DepartmentDto dto) {
        var parentId = dto.getParentDepartmentId();
        if (parentId != null) {
            if (!departmentRepository.existsById(parentId)) {
                throw new ResourceNotFoundException(
                        "Couldn't find parent department with id " + parentId + ".");
            }
        }

        try {
            var entity = departmentMapper.toEntity(dto);
            if (parentId != null) {
                var parentEntity = departmentRepository.findById(parentId);
                entity.setParentDepartment(parentEntity.get());
            }
            entity = departmentRepository.saveAndFlush(entity);
            dto.setId(entity.getId());
            return dto;
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidDataException(
                    "There is already department with name " + dto.getName() + ".");
        }
    }

    @Override
    public void delete(Integer id) {
        checkIfTheDepartmentIdExists(id);

        try {
            departmentRepository.deleteById(id);
            entityManager.flush();
        } catch (ConstraintViolationException ex) {
            throw new InvalidDeletionException(
                    "Couldn't delete department with id " + id + "." +
                    " The department still has employees.");
        }
    }

    private void checkIfTheDepartmentIdExists(Integer departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException(
                    "Couldn't find department with id " + departmentId + ".");
        }
    }

}
