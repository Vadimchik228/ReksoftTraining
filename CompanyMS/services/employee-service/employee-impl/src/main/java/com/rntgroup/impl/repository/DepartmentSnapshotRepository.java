package com.rntgroup.impl.repository;

import com.rntgroup.impl.entity.DepartmentSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentSnapshotRepository extends JpaRepository<DepartmentSnapshot, Integer> {
}
