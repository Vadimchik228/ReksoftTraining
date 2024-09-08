package com.rntgroup.impl.repository;

import com.rntgroup.impl.entity.DepartmentPayroll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentPayrollRepository extends
        JpaRepository<DepartmentPayroll, Long> {
}
