package com.rntgroup.database.repository;

import com.rntgroup.database.entity.DepartmentPayroll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentPayrollRepository extends
        JpaRepository<DepartmentPayroll, Long> {
}
