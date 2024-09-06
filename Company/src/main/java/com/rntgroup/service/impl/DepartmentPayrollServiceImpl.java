package com.rntgroup.service.impl;

import com.rntgroup.database.entity.Department;
import com.rntgroup.database.entity.DepartmentPayroll;
import com.rntgroup.database.repository.DepartmentPayrollRepository;
import com.rntgroup.database.repository.DepartmentRepository;
import com.rntgroup.database.repository.EmployeeRepository;
import com.rntgroup.service.DepartmentPayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentPayrollServiceImpl implements DepartmentPayrollService {
    private static final long NUMBER_OF_MILLISECONDS = 300000L; // 5 минут

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentPayrollRepository departmentPayrollRepository;

    @Scheduled(fixedRate = NUMBER_OF_MILLISECONDS)
    @Override
    public void calculateDepartmentPayrolls() {
        var departments = departmentRepository.findAll();
        var currentTimestamp = LocalDateTime.now();

        for (Department department : departments) {
            var salaryFund = employeeRepository.sumSalariesByDepartmentId(
                    department.getId()
            );
            if (salaryFund == null) salaryFund = BigDecimal.ZERO;

            var departmentPayroll = DepartmentPayroll.builder()
                    .departmentId(department.getId())
                    .salaryFund(salaryFund)
                    .timestamp(currentTimestamp)
                    .build();

            departmentPayrollRepository.save(departmentPayroll);
        }
    }
}
