package com.rntgroup.impl.integration.repository;

import com.rntgroup.impl.integration.IntegrationTestBase;
import com.rntgroup.impl.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor
public class DepartmentRepositoryIT extends IntegrationTestBase {

    private final DepartmentRepository departmentRepository;

    @Test
    void findByName() {
        var department = departmentRepository.findByName(
                "Департамент финансов и управления");
        assertThat(department).isPresent();
        assertThat(department.get().getName()).isEqualTo(
                "Департамент финансов и управления");

        department = departmentRepository.findByName(
                "Несуществующий департамент");
        assertThat(department).isEmpty();
    }

    @Test
    void findAllByDepartmentHierarchy() {
        var pageable = PageRequest.of(0, 4,
                Sort.by(Sort.Direction.DESC, "level", "name"));
        var departments = departmentRepository.findAllByDepartmentHierarchy(pageable)
                .getContent();

        assertThat(departments.size()).isEqualTo(4);
        assertThat(departments.get(0).getId()).isEqualTo(6);
        assertThat(departments.get(1).getId()).isEqualTo(3);
        assertThat(departments.get(2).getId()).isEqualTo(4);
        assertThat(departments.get(3).getId()).isEqualTo(2);
    }
}
