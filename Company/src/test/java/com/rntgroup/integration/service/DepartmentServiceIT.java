package com.rntgroup.integration.service;

import com.rntgroup.database.repository.DepartmentRepository;
import com.rntgroup.integration.IntegrationTestBase;
import com.rntgroup.service.DepartmentService;
import com.rntgroup.exception.InvalidDataException;
import com.rntgroup.exception.InvalidDeletionException;
import com.rntgroup.exception.ResourceNotFoundException;
import com.rntgroup.web.dto.department.DepartmentDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@RequiredArgsConstructor
class DepartmentServiceIT extends IntegrationTestBase {
    private static final int FIRST_DEPARTMENT_ID = 1;
    private static final String FIRST_DEPARTMENT_NAME = "Департамент финансов и управления";
    private static final int DEPENDENT_DEPARTMENT_ID = 2;
    private static final int INDEPENDENT_DEPARTMENT_ID = 6;
    private static final int NON_EXISTENT_DEPARTMENT_ID = 100;

    private final DepartmentService departmentService;
    private final DepartmentRepository departmentRepository;

    @Test
    void getById() {
        var departmentDto = departmentService.getById(FIRST_DEPARTMENT_ID);
        assertThat(departmentDto.getId()).isEqualTo(FIRST_DEPARTMENT_ID);
        assertThat(departmentDto.getName()).isEqualTo(FIRST_DEPARTMENT_NAME);
    }

    @Test
    void getById_notFound() {
        assertThatThrownBy(() -> departmentService.getById(NON_EXISTENT_DEPARTMENT_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find department with id " +
                        NON_EXISTENT_DEPARTMENT_ID + ".");
    }

    @Test
    void getByName() {
        var departmentDto = departmentService.getByName(FIRST_DEPARTMENT_NAME);
        assertThat(departmentDto).isPresent();
        assertThat(departmentDto.get().getId()).isEqualTo(FIRST_DEPARTMENT_ID);
    }

    @Test
    void getByName_notFound() {
        var departmentDto = departmentService.getByName("Несуществующий департамент");
        assertThat(departmentDto).isEmpty();
    }

    @Test
    void getAll() {
        var pageable = PageRequest.of(0, 4,
                Sort.by(Sort.Direction.ASC, "level", "name"));
        var page = departmentService.getAll(pageable);

        assertThat(page.getTotalElements()).isEqualTo(6);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void update() {
        var departmentDto = new DepartmentDto();
        departmentDto.setId(FIRST_DEPARTMENT_ID);
        departmentDto.setName("Обновленный департамент");
        departmentDto.setCreationDate(LocalDate.now());

        var updatedDepartmentDto = departmentService.update(departmentDto);
        assertThat(updatedDepartmentDto.getId()).isEqualTo(FIRST_DEPARTMENT_ID);
        assertThat(updatedDepartmentDto.getName()).isEqualTo(
                "Обновленный департамент");
    }

    @Test
    void update_notFound() {
        var departmentDto = new DepartmentDto();
        departmentDto.setId(NON_EXISTENT_DEPARTMENT_ID);
        departmentDto.setName("Обновленный департамент");
        departmentDto.setCreationDate(LocalDate.now());

        assertThatThrownBy(() -> departmentService.update(departmentDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find department with id " +
                        NON_EXISTENT_DEPARTMENT_ID + ".");
    }

    @Test
    void update_invalidParentDepartment() {
        var departmentDto = new DepartmentDto();
        departmentDto.setId(FIRST_DEPARTMENT_ID);
        departmentDto.setName("Обновленный департамент");
        departmentDto.setCreationDate(LocalDate.now());
        departmentDto.setParentDepartmentId(NON_EXISTENT_DEPARTMENT_ID);

        assertThatThrownBy(() -> departmentService.update(departmentDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find parent department with id " +
                        NON_EXISTENT_DEPARTMENT_ID + ".");
    }

    @Test
    void update_duplicateName() {
        var departmentDto = new DepartmentDto();
        departmentDto.setId(2);
        departmentDto.setName(FIRST_DEPARTMENT_NAME);
        departmentDto.setCreationDate(LocalDate.now());

        assertThatThrownBy(() -> departmentService.update(departmentDto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "There is already department with name " +
                        FIRST_DEPARTMENT_NAME + ".");
    }

    @Test
    void create() {
        var departmentDto = new DepartmentDto();
        departmentDto.setName("Новый департамент");
        departmentDto.setParentDepartmentId(2);
        departmentDto.setCreationDate(LocalDate.now());

        var createdDepartmentDto = departmentService.create(departmentDto);
        assertThat(createdDepartmentDto.getId()).isNotNull();
        assertThat(createdDepartmentDto.getName()).isEqualTo("Новый департамент");
        assertThat(createdDepartmentDto.getParentDepartmentId()).isEqualTo(2);
    }

    @Test
    void create_duplicateName() {
        var departmentDto = new DepartmentDto();
        departmentDto.setName(FIRST_DEPARTMENT_NAME);
        departmentDto.setCreationDate(LocalDate.now());

        assertThatThrownBy(() -> departmentService.create(departmentDto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "There is already department with name " +
                        FIRST_DEPARTMENT_NAME + ".");
    }

    @Test
    void create_invalidParentDepartment() {
        var departmentDto = new DepartmentDto();
        departmentDto.setName("Новый департамент");
        departmentDto.setCreationDate(LocalDate.now());
        departmentDto.setParentDepartmentId(NON_EXISTENT_DEPARTMENT_ID);

        assertThatThrownBy(() -> departmentService.create(departmentDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find parent department with id " +
                        NON_EXISTENT_DEPARTMENT_ID + ".");
    }

    @Test
    void delete() {
        departmentService.delete(INDEPENDENT_DEPARTMENT_ID);
        var deletedEntity = departmentRepository.findById(INDEPENDENT_DEPARTMENT_ID);
        assertThat(deletedEntity).isEmpty();
    }

    @Test
    void delete_notFound() {
        assertThatThrownBy(() -> departmentService.getById(NON_EXISTENT_DEPARTMENT_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find department with id " +
                        NON_EXISTENT_DEPARTMENT_ID + ".");
    }

    @Test
    void delete_withDependentEmployees() {
        assertThatThrownBy(() -> departmentService.delete(DEPENDENT_DEPARTMENT_ID))
                .isInstanceOf(InvalidDeletionException.class)
                .hasMessageContaining(
                        "Couldn't delete department with id " +
                        DEPENDENT_DEPARTMENT_ID + ".");
    }
}
