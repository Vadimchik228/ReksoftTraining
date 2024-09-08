package com.rntgroup.impl.repository;

import com.rntgroup.impl.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query(value = """
            select count(e.id)
            from Employee e
            where e.departmentId = :departmentId
              and (e.dismissalDate is null or e.dismissalDate > current_date)
            """)
    Long countAllByDepartmentId(@Param("departmentId") Integer departmentId);

    @Query(value = """
            select sum(e.salary)
            from Employee e
            where e.departmentId = :departmentId
              and (e.dismissalDate is null or e.dismissalDate > current_date)
            """)
    BigDecimal sumSalariesByDepartmentId(@Param("departmentId") Integer departmentId);

    @Query(value = """
            SELECT e.*
            FROM employee e
            WHERE e.department_id = :departmentId
              AND e.director = TRUE
              AND e.dismissal_date IS NULL
            """, nativeQuery = true)
    Optional<Employee> findDirectorByDepartmentId(@Param("departmentId") Integer departmentId);

    @Query(value = """
            select e
            from Employee e
            where e.departmentId = :departmentId
              and (e.dismissalDate is null or e.dismissalDate > current_date)
            """)
    Page<Employee> findAllByDepartmentId(@Param("departmentId") Integer departmentId, Pageable pageable);

    @Query(value = """
            select max(e.salary)
            from Employee e
            where e.departmentId = :departmentId
              and e.isDirector = false
              and (e.dismissalDate is null or e.dismissalDate > current_date)
            """)
    BigDecimal findMaxSalaryOfNonDirectorByDepartmentId(@Param("departmentId") Integer departmentId);
}
