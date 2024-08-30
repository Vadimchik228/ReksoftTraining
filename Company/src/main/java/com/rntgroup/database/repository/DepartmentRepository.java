package com.rntgroup.database.repository;

import com.rntgroup.database.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Optional<Department> findByName(@Param("name") String name);

    @Query(value = """
            WITH RECURSIVE department_hierarchy (
                                                 id,
                                                 name,
                                                 creation_date,
                                                 parent_department_id,
                                                 level)
                               AS (SELECT id,
                                          name,
                                          creation_date,
                                          parent_department_id,
                                          0
                                   FROM department
                                   WHERE parent_department_id IS NULL
                                   UNION ALL
                                   SELECT d.id,
                                          d.name,
                                          d.creation_date,
                                          d.parent_department_id,
                                          level + 1
                                   FROM department d
                                   JOIN department_hierarchy dh
                                     ON d.parent_department_id = dh.id)
            SELECT dh.id,
                   dh.name,
                   dh.creation_date,
                   dh.parent_department_id
            FROM department_hierarchy dh
                        """,
            countQuery = "SELECT COUNT(*) FROM department",
            nativeQuery = true)
    Page<Department> findAllByDepartmentHierarchy(Pageable pageable);
}
