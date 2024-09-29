package com.rntgroup.impl.integration.service;

import com.rntgroup.impl.entity.Department;
import com.rntgroup.impl.integration.IntegrationTestBase;
import com.rntgroup.impl.repository.DepartmentPayrollRepository;
import com.rntgroup.impl.repository.DepartmentRepository;
import com.rntgroup.impl.service.DepartmentPayrollService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
public class DepartmentPayrollServiceIT extends IntegrationTestBase {
    private static final int FIRST_DEPARTMENT_ID = 1;
    private static final BigDecimal FIRST_DEPARTMENT_SALARY_FUND
            = new BigDecimal("515000.00");

    private final DepartmentPayrollService service;
    private final DepartmentRepository departmentRepository;
    private final DepartmentPayrollRepository departmentPayrollRepository;

    @Test
    void calculateDepartmentPayrolls() {
        service.calculateDepartmentPayrolls();

        var departmentPayrolls = departmentPayrollRepository.findAll();

        var departments = departmentRepository.findAll();
        var departmentIds = departments.stream()
                .map(Department::getId)
                .toList();

        assertThat(departmentPayrolls.size()).isEqualTo(6);

        for (var departmentPayroll : departmentPayrolls) {
            assertTrue(departmentIds.contains(departmentPayroll.getDepartmentId()));
        }

        var salaryFund = departmentPayrolls.stream()
                .filter(it -> it.getDepartmentId().equals(FIRST_DEPARTMENT_ID))
                .findFirst()
                .get()
                .getSalaryFund();

        assertThat(salaryFund).isEqualTo(FIRST_DEPARTMENT_SALARY_FUND);
    }
}
