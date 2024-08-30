package com.rntgroup.integration.repository;

import com.rntgroup.database.repository.EmployeeRepository;
import com.rntgroup.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor
class EmployeeRepositoryTest extends IntegrationTestBase {
    private static final int DEPARTMENT_ID = 1;
    private final EmployeeRepository employeeRepository;

    @Test
    void countAllByDepartmentId() {
        Long count = employeeRepository.countAllByDepartmentId(1);
        assertThat(count).isEqualTo(6L);
    }

    @Test
    void sumSalariesByDepartmentId() {
        var sum = employeeRepository.sumSalariesByDepartmentId(DEPARTMENT_ID);
        assertThat(sum).isEqualTo(new BigDecimal("515000.00"));
    }

    @Test
    void findDirectorByDepartmentId() {
        var director = employeeRepository.findDirectorByDepartmentId(DEPARTMENT_ID);
        assertThat(director).isPresent();
        assertThat(director.get().getIsDirector()).isTrue();
    }

    @Test
    void findAllByDepartmentId() {
        var pageable = PageRequest.of(0, 5,
                Sort.by(Sort.Direction.DESC, "isDirector"));
        var employees = employeeRepository.findAllByDepartmentId(DEPARTMENT_ID, pageable);
        assertThat(employees.getTotalElements()).isEqualTo(6);
        assertThat(employees.getTotalPages()).isEqualTo(2);
        assertThat(employees.getContent().get(0).getId()).isEqualTo(1);
    }

    @Test
    void findMaxSalaryOfNonDirectorByDepartmentId() {
        var maxSalary = employeeRepository.findMaxSalaryOfNonDirectorByDepartmentId(
                DEPARTMENT_ID);
        assertThat(maxSalary).isEqualTo(new BigDecimal("100000.00"));
    }
}
