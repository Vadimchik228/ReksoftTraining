package com.rntgroup.impl.unit.service;

import com.rntgroup.api.client.EmployeeClient;
import com.rntgroup.impl.entity.Department;
import com.rntgroup.impl.repository.DepartmentPayrollRepository;
import com.rntgroup.impl.repository.DepartmentRepository;
import com.rntgroup.impl.service.impl.DepartmentPayrollServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentPayrollServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeClient employeeClient;

    @Mock
    private DepartmentPayrollRepository departmentPayrollRepository;

    @InjectMocks
    private DepartmentPayrollServiceImpl departmentPayrollService;

    @Test
    void calculateDepartmentPayrolls() {
        var department = getDepartment();
        var departments = List.of(department);
        var salaryFund = BigDecimal.valueOf(100000);


        when(departmentRepository.findAll())
                .thenReturn(departments);
        when(employeeClient.getSalaryFundByDepartmentId(department.getId()))
                .thenReturn(salaryFund);

        departmentPayrollService.calculateDepartmentPayrolls();

        verify(departmentRepository, times(1))
                .findAll();
        verify(employeeClient, times(1))
                .getSalaryFundByDepartmentId(department.getId());
        verify(departmentPayrollRepository, times(1))
                .save(argThat(departmentPayroll ->
                                departmentPayroll.getDepartmentId()
                                        .equals(department.getId())
                                && departmentPayroll.getSalaryFund()
                                        .equals(salaryFund)
                        )
                );
    }

    private Department getDepartment() {
        return Department.builder()
                .id(1)
                .name("Департамент финансов и управления")
                .creationDate(LocalDate.now())
                .parentDepartment(null)
                .build();
    }
}
