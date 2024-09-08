package com.rntgroup.impl.service.impl;

import com.rntgroup.api.client.EmployeeClient;
import com.rntgroup.impl.entity.Department;
import com.rntgroup.impl.entity.DepartmentPayroll;
import com.rntgroup.impl.repository.DepartmentPayrollRepository;
import com.rntgroup.impl.repository.DepartmentRepository;
import com.rntgroup.impl.service.DepartmentPayrollService;
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
    private final EmployeeClient employeeClient;
    private final DepartmentPayrollRepository departmentPayrollRepository;

    @Scheduled(
            initialDelay = NUMBER_OF_MILLISECONDS,
            fixedRate = NUMBER_OF_MILLISECONDS
    )
    @Override
    public void calculateDepartmentPayrolls() {
        var departments = departmentRepository.findAll();
        var currentTimestamp = LocalDateTime.now();

        for (Department department : departments) {
            var salaryFund = employeeClient.getSalaryFundByDepartmentId(
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
