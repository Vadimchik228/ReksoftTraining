package com.rntgroup.impl.service.impl;

import com.rntgroup.api.client.EmployeeClient;
import com.rntgroup.api.dto.DepartmentDto;
import com.rntgroup.api.exception.FeignClientNotFoundException;
import com.rntgroup.api.exception.InvalidDataException;
import com.rntgroup.api.exception.InvalidDeletionException;
import com.rntgroup.api.exception.ResourceNotFoundException;
import com.rntgroup.impl.kafka.DepartmentProducer;
import com.rntgroup.impl.mapper.DepartmentMapper;
import com.rntgroup.impl.repository.DepartmentRepository;
import com.rntgroup.impl.service.DepartmentService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
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
    private final EmployeeClient employeeClient;
    private final DepartmentProducer departmentProducer;

    @Override
    @Transactional(readOnly = true)
    public DepartmentDto getById(final Integer id) {
        return departmentRepository.findById(id)
                .map(departmentMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                                "Couldn't find department with id " + id + "."
                        )
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepartmentDto> getByName(final String name) {
        return departmentRepository.findByName(name)
                .map(departmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentDto> getAll(final Pageable pageable) {
        return departmentRepository.findAllByDepartmentHierarchy(pageable)
                .map(departmentMapper::toDto);
    }

    @Override
    public DepartmentDto update(final DepartmentDto dto) {
        checkIfDepartmentIdExists(dto.getId());

        var parentId = dto.getParentDepartmentId();
        if (parentId != null) {
            if (!departmentRepository.existsById(parentId)) {
                throw new ResourceNotFoundException(
                        "Couldn't find parent department with id " + parentId + "."
                );
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
                    "There is already department with name " + dto.getName() + "."
            );
        }
    }

    @Override
    public DepartmentDto create(DepartmentDto dto) {
        var parentId = dto.getParentDepartmentId();
        if (parentId != null) {
            if (!departmentRepository.existsById(parentId)) {
                throw new ResourceNotFoundException(
                        "Couldn't find parent department with id " + parentId + "."
                );
            }
        }

        try {
            var entity = departmentMapper.toEntity(dto);
            if (parentId != null) {
                var parentEntity = departmentRepository.findById(parentId);
                entity.setParentDepartment(parentEntity.get());
            }
            entity = departmentRepository.saveAndFlush(entity);

            departmentProducer.sendDepartmentCreated(entity);

            dto.setId(entity.getId());
            return dto;
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidDataException(
                    "There is already department with name " + dto.getName() + "."
            );
        }
    }

    @Override
    public void delete(final Integer id) {
        checkIfDepartmentIdExists(id);

        var employeesCount = employeeClient.getCountOfAllByDepartmentId(id);

        if (employeesCount == null) {
            throw new FeignClientNotFoundException(
                    "Couldn't count all employees of department with id " + id + "."
            );
        }

        if (employeesCount > 0L) {
            throw new InvalidDeletionException(
                    "Couldn't delete department with id " + id + "." +
                    " There are dependent employees in the DB."
            );
        }

        departmentRepository.deleteById(id);
        entityManager.flush();

        departmentProducer.sendDepartmentDeleted(id);
    }

    private void checkIfDepartmentIdExists(final Integer departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException(
                    "Couldn't find department with id " + departmentId + "."
            );
        }
    }

}
