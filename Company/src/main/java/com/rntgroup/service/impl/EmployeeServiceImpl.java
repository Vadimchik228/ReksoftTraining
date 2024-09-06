package com.rntgroup.service.impl;

import com.rntgroup.database.entity.Department;
import com.rntgroup.database.entity.Employee;
import com.rntgroup.database.entity.Position;
import com.rntgroup.database.repository.DepartmentRepository;
import com.rntgroup.database.repository.EmployeeRepository;
import com.rntgroup.database.repository.PositionRepository;
import com.rntgroup.exception.InvalidDataException;
import com.rntgroup.exception.ResourceNotFoundException;
import com.rntgroup.service.EmployeeService;
import com.rntgroup.web.dto.employee.EmployeeDto;
import com.rntgroup.web.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto getById(final Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                                "Couldn't find employee with id " + id + "."
                        )
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeDto> getDirectorByDepartmentId(final Integer departmentId) {
        checkIfDepartmentIdIsSetCorrectly(departmentId);
        return employeeRepository.findDirectorByDepartmentId(departmentId)
                .map(employeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDto> getAllByDepartmentId(
            final Integer departmentId, final Pageable pageable) {
        return employeeRepository.findAllByDepartmentId(departmentId, pageable)
                .map(employeeMapper::toDto);
    }

    @Override
    public EmployeeDto update(final EmployeeDto dto) {
        checkIfEmployeeIdExists(dto.getId());

        var position = checkIfPositionIdIsSetCorrectly(dto.getPositionId());

        var departmentId = dto.getDepartmentId();
        var department = checkIfDepartmentIdIsSetCorrectly(departmentId);

        var director = employeeRepository.findDirectorByDepartmentId(departmentId);
        // В случае, если изменяется непосредственно сам директор,
        // эти проверки осуществлять не надо
        if (director.isPresent() && !dto.getId().equals(director.get().getId())) {
            checkIfOrdinalEmployeeDidntTakeDirectorPlace(dto, director.get());
        }

        checkIfDirectorSalaryIsHigherThanOrdinaryOne(dto);

        var employee = employeeMapper.toEntity(dto);
        employee.setPosition(position);
        employee.setDepartment(department);

        employeeRepository.saveAndFlush(employee);
        return dto;
    }

    @Override
    public EmployeeDto create(EmployeeDto dto) {
        var position = checkIfPositionIdIsSetCorrectly(dto.getPositionId());

        var departmentId = dto.getDepartmentId();
        var department = checkIfDepartmentIdIsSetCorrectly(departmentId);

        var director = employeeRepository.findDirectorByDepartmentId(departmentId);
        director.ifPresent(employee ->
                checkIfOrdinalEmployeeDidntTakeDirectorPlace(dto, employee));

        checkIfDirectorSalaryIsHigherThanOrdinaryOne(dto);

        var employee = employeeMapper.toEntity(dto);
        employee.setPosition(position);
        employee.setDepartment(department);

        employee = employeeRepository.saveAndFlush(employee);

        dto.setId(employee.getId());
        return dto;
    }

    @Override
    public void delete(final Long id) {
        checkIfEmployeeIdExists(id);
        employeeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAllByDepartmentId(final Integer departmentId) {
        checkIfDepartmentIdIsSetCorrectly(departmentId);
        var employeesCount = employeeRepository.countAllByDepartmentId(departmentId);
        return employeesCount == null ? 0L : employeesCount;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal sumSalariesByDepartmentId(final Integer departmentId) {
        checkIfDepartmentIdIsSetCorrectly(departmentId);
        var salaryFund = employeeRepository.sumSalariesByDepartmentId(departmentId);
        return salaryFund == null ? BigDecimal.ZERO : salaryFund;
    }

    private void checkIfEmployeeIdExists(final Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException(
                    "Couldn't find employee with id " + employeeId + "."
            );
        }
    }

    private Position checkIfPositionIdIsSetCorrectly(final Integer positionId) {
        return positionRepository.findById(positionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                                "Couldn't find position with id " + positionId + "."
                        )
                );
    }

    private Department checkIfDepartmentIdIsSetCorrectly(final Integer departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                                "Couldn't find department with id " + departmentId + "."
                        )
                );
    }

    private void checkIfOrdinalEmployeeDidntTakeDirectorPlace(
            final EmployeeDto dto, final Employee director) {
        if (dto.getIsDirector()) {
            throw new InvalidDataException(
                    "The department with id " + dto.getDepartmentId() +
                    " already has a director."
            );
        }

        if (dto.getSalary().compareTo(director.getSalary()) > 0) {
            throw new InvalidDataException(
                    "The employee's salary cannot be higher than the director's salary "
                    + director.getSalary() + "."
            );
        }
    }

    private void checkIfDirectorSalaryIsHigherThanOrdinaryOne(final EmployeeDto dto) {
        if (dto.getIsDirector()) {
            var maxSalary = employeeRepository
                    .findMaxSalaryOfNonDirectorByDepartmentId(dto.getDepartmentId());
            if (dto.getSalary().compareTo(maxSalary) < 0) {
                throw new InvalidDataException(
                        "The director's salary cannot be lower than the employer's salary "
                        + maxSalary + "."
                );
            }
        }
    }

}
