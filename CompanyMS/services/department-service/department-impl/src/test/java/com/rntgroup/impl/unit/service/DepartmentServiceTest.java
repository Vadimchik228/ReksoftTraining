package com.rntgroup.impl.unit.service;

import com.rntgroup.api.client.EmployeeClient;
import com.rntgroup.api.dto.DepartmentDto;
import com.rntgroup.api.exception.InvalidDataException;
import com.rntgroup.api.exception.InvalidDeletionException;
import com.rntgroup.api.exception.ResourceNotFoundException;
import com.rntgroup.impl.entity.Department;
import com.rntgroup.impl.kafka.DepartmentProducer;
import com.rntgroup.impl.mapper.DepartmentMapper;
import com.rntgroup.impl.repository.DepartmentRepository;
import com.rntgroup.impl.service.impl.DepartmentServiceImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {
    private static final int FIRST_DEPARTMENT_ID = 1;
    private static final String FIRST_DEPARTMENT_NAME =
            "Департамент финансов и управления";
    private static final int DEPENDENT_DEPARTMENT_ID = 2;
    private static final int INDEPENDENT_DEPARTMENT_ID = 6;
    private static final int NON_EXISTENT_DEPARTMENT_ID = 100;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @Mock
    private EntityManager entityManager;

    @Mock
    private EmployeeClient employeeClient;

    @Mock
    private DepartmentProducer departmentProducer;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Test
    void getById() {
        var department = getDepartment(
                FIRST_DEPARTMENT_ID,
                FIRST_DEPARTMENT_NAME
        );
        var departmentDto = getDepartmentDto(
                FIRST_DEPARTMENT_ID,
                FIRST_DEPARTMENT_NAME
        );

        when(departmentRepository.findById(FIRST_DEPARTMENT_ID))
                .thenReturn(Optional.of(department));
        when(departmentMapper.toDto(department))
                .thenReturn(departmentDto);

        var result = departmentService.getById(FIRST_DEPARTMENT_ID);

        assertThat(result).isEqualTo(departmentDto);
    }

    @Test
    void getById_notFound() {
        when(departmentRepository.findById(NON_EXISTENT_DEPARTMENT_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.getById(NON_EXISTENT_DEPARTMENT_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find department with id "
                        + NON_EXISTENT_DEPARTMENT_ID + "."
                );
    }

    @Test
    void getByName() {
        var department = getDepartment(
                FIRST_DEPARTMENT_ID,
                FIRST_DEPARTMENT_NAME
        );
        var departmentDto = getDepartmentDto(
                FIRST_DEPARTMENT_ID,
                FIRST_DEPARTMENT_NAME
        );

        when(departmentRepository.findByName(FIRST_DEPARTMENT_NAME))
                .thenReturn(Optional.of(department));
        when(departmentMapper.toDto(department))
                .thenReturn(departmentDto);

        var result = departmentService.getByName(FIRST_DEPARTMENT_NAME);

        assertThat(result.get()).isEqualTo(departmentDto);
    }

    @Test
    void getByName_notFound() {
        when(departmentRepository.findByName("Несуществующий департамент"))
                .thenReturn(Optional.empty());

        var result = departmentService.getByName("Несуществующий департамент");

        assertThat(result).isEmpty();
    }

    @Test
    void getAll() {
        var pageable = PageRequest.of(0, 4,
                Sort.by(Sort.Direction.ASC, "level", "name"));

        var department1 = getDepartment(1, FIRST_DEPARTMENT_NAME);
        var department2 = getDepartment(2, "Департамент информационных технологий");
        var department3 = getDepartment(3, "Департамент закупок");
        var department4 = getDepartment(4, "Департамент кадров");

        var departmentDto1 = getDepartmentDto(1, FIRST_DEPARTMENT_NAME);
        var departmentDto2 = getDepartmentDto(2, "Департамент информационных технологий");
        var departmentDto3 = getDepartmentDto(3, "Департамент закупок");
        var departmentDto4 = getDepartmentDto(4, "Департамент кадров");


        var departments = List.of(department1, department2, department3, department4);
        var page = new PageImpl<>(departments, pageable, departments.size());

        when(departmentRepository.findAllByDepartmentHierarchy(pageable)).thenReturn(page);
        when(departmentMapper.toDto(department1)).thenReturn(departmentDto1);
        when(departmentMapper.toDto(department2)).thenReturn(departmentDto2);
        when(departmentMapper.toDto(department3)).thenReturn(departmentDto3);
        when(departmentMapper.toDto(department4)).thenReturn(departmentDto4);

        var result = departmentService.getAll(pageable);

        assertThat(result.getTotalElements())
                .isEqualTo(4);
        assertThat(result.getTotalPages())
                .isEqualTo(1);

        verify(departmentRepository, times(1))
                .findAllByDepartmentHierarchy(pageable);
        verify(departmentMapper, times(4))
                .toDto(any());
    }

    @Test
    void update() {
        var departmentDto = getDepartmentDto(
                FIRST_DEPARTMENT_ID,
                "Обновленный департамент"
        );
        var department = getDepartment(
                FIRST_DEPARTMENT_ID,
                "Обновленный департамент"
        );

        when(departmentRepository.existsById(FIRST_DEPARTMENT_ID))
                .thenReturn(true);
        when(departmentRepository.saveAndFlush(any(Department.class)))
                .thenReturn(department);
        when(departmentMapper.toEntity(departmentDto))
                .thenReturn(department);

        var updatedDepartmentDto = departmentService.update(departmentDto);

        assertThat(updatedDepartmentDto.getId())
                .isEqualTo(FIRST_DEPARTMENT_ID);
        assertThat(updatedDepartmentDto.getName())
                .isEqualTo("Обновленный департамент");

        verify(departmentRepository, times(1))
                .existsById(FIRST_DEPARTMENT_ID);
        verify(departmentRepository, times(1))
                .saveAndFlush(department);
        verify(departmentMapper, times(1))
                .toEntity(departmentDto);
    }

    @Test
    void update_notFound() {
        var departmentDto = getDepartmentDto(
                NON_EXISTENT_DEPARTMENT_ID,
                "Обновленный департамент"
        );

        when(departmentRepository.existsById(NON_EXISTENT_DEPARTMENT_ID))
                .thenReturn(false);

        assertThatThrownBy(() -> departmentService.update(departmentDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find department with id " +
                        NON_EXISTENT_DEPARTMENT_ID + "."
                );

        verify(departmentRepository, times(1))
                .existsById(NON_EXISTENT_DEPARTMENT_ID);
        verify(departmentRepository, never()).saveAndFlush(any());
        verify(departmentMapper, never()).toEntity(any());
    }

    @Test
    void update_invalidParentDepartment() {
        var departmentDto = getDepartmentDto(
                FIRST_DEPARTMENT_ID,
                "Обновленный департамент"
        );
        departmentDto.setParentDepartmentId(NON_EXISTENT_DEPARTMENT_ID);

        when(departmentRepository.existsById(FIRST_DEPARTMENT_ID))
                .thenReturn(true);
        when(departmentRepository.existsById(NON_EXISTENT_DEPARTMENT_ID))
                .thenReturn(false);

        assertThatThrownBy(() -> departmentService.update(departmentDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find parent department with id "
                        + NON_EXISTENT_DEPARTMENT_ID + "."
                );

        verify(departmentRepository, times(1))
                .existsById(FIRST_DEPARTMENT_ID);
        verify(departmentRepository, times(1))
                .existsById(NON_EXISTENT_DEPARTMENT_ID);
        verify(departmentRepository, never())
                .saveAndFlush(any());
        verify(departmentMapper, never())
                .toEntity(any());
    }

    @Test
    void update_duplicateName() {
        var departmentDto = getDepartmentDto(
                2,
                FIRST_DEPARTMENT_NAME
        );

        when(departmentRepository.existsById(2))
                .thenReturn(true);
        doThrow(DataIntegrityViolationException.class)
                .when(departmentRepository).saveAndFlush(any());

        assertThatThrownBy(() -> departmentService.update(departmentDto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "There is already department with name "
                        + FIRST_DEPARTMENT_NAME + "."
                );

        verify(departmentRepository, times(1))
                .existsById(2);
        verify(departmentRepository, times(1))
                .saveAndFlush(any());
        verify(departmentMapper, times(1))
                .toEntity(departmentDto);
    }

    @Test
    void create() {
        var departmentDto = getDepartmentDto(
                null,
                "Новый департамент"
        );
        var department = getDepartment(
                null,
                "Новый департамент"
        );

        when(departmentRepository.saveAndFlush(any(Department.class)))
                .thenReturn(department);
        when(departmentMapper.toEntity(departmentDto))
                .thenReturn(department);

        doNothing().when(departmentProducer)
                .sendDepartmentCreated(department);

        var createdDepartmentDto = departmentService.create(departmentDto);

        assertThat(createdDepartmentDto.getId())
                .isEqualTo(department.getId());

        verify(departmentRepository, times(1))
                .saveAndFlush(department);
        verify(departmentMapper, times(1))
                .toEntity(departmentDto);
    }

    @Test
    void create_duplicateName() {
        var departmentDto = getDepartmentDto(
                null,
                FIRST_DEPARTMENT_NAME
        );
        var department = getDepartment(
                null,
                FIRST_DEPARTMENT_NAME
        );

        when(departmentRepository.saveAndFlush(any(Department.class)))
                .thenThrow(DataIntegrityViolationException.class);
        when(departmentMapper.toEntity(departmentDto))
                .thenReturn(department);

        assertThatThrownBy(() -> departmentService.create(departmentDto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "There is already department with name "
                        + FIRST_DEPARTMENT_NAME + "."
                );

        verify(departmentRepository, times(1))
                .saveAndFlush(any());
        verify(departmentMapper, times(1))
                .toEntity(departmentDto);
    }

    @Test
    void create_invalidParentDepartment() {
        var departmentDto = getDepartmentDto(
                null,
                "Новый департамент"
        );
        departmentDto.setParentDepartmentId(NON_EXISTENT_DEPARTMENT_ID);

        when(departmentRepository.existsById(NON_EXISTENT_DEPARTMENT_ID))
                .thenReturn(false);

        assertThatThrownBy(() -> departmentService.create(departmentDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find parent department with id "
                        + NON_EXISTENT_DEPARTMENT_ID + "."
                );

        verify(departmentRepository, times(1))
                .existsById(NON_EXISTENT_DEPARTMENT_ID);
        verify(departmentRepository, never())
                .saveAndFlush(any());
        verify(departmentMapper, never())
                .toEntity(any());
    }

    @Test
    void delete() {
        when(departmentRepository.existsById(INDEPENDENT_DEPARTMENT_ID))
                .thenReturn(true);

        doNothing().when(departmentProducer)
                .sendDepartmentDeleted(INDEPENDENT_DEPARTMENT_ID);
        doNothing().when(entityManager)
                .flush();

        departmentService.delete(INDEPENDENT_DEPARTMENT_ID);

        verify(departmentRepository, times(1))
                .existsById(INDEPENDENT_DEPARTMENT_ID);
        verify(departmentRepository, times(1))
                .deleteById(INDEPENDENT_DEPARTMENT_ID);
    }

    @Test
    void delete_notFound() {
        when(departmentRepository.existsById(NON_EXISTENT_DEPARTMENT_ID))
                .thenReturn(false);

        assertThatThrownBy(() -> departmentService.delete(NON_EXISTENT_DEPARTMENT_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find department with id "
                        + NON_EXISTENT_DEPARTMENT_ID + "."
                );

        verify(departmentRepository, times(1))
                .existsById(NON_EXISTENT_DEPARTMENT_ID);
        verify(departmentRepository, never())
                .deleteById(NON_EXISTENT_DEPARTMENT_ID);
    }

    @Test
    void delete_withDependentEmployees() {
        when(departmentRepository.existsById(DEPENDENT_DEPARTMENT_ID))
                .thenReturn(true);
        when(employeeClient.getCountOfAllByDepartmentId(DEPENDENT_DEPARTMENT_ID))
                .thenReturn(1L);

        assertThatThrownBy(() -> departmentService.delete(DEPENDENT_DEPARTMENT_ID))
                .isInstanceOf(InvalidDeletionException.class)
                .hasMessageContaining(
                        "Couldn't delete department with id "
                        + DEPENDENT_DEPARTMENT_ID + "."
                );

        verify(departmentRepository, times(1))
                .existsById(DEPENDENT_DEPARTMENT_ID);
        verify(employeeClient, times(1))
                .getCountOfAllByDepartmentId(DEPENDENT_DEPARTMENT_ID);
        verify(departmentRepository, never())
                .deleteById(DEPENDENT_DEPARTMENT_ID);
    }

    private Department getDepartment(final Integer id, final String name) {
        return Department.builder()
                .id(id)
                .name(name)
                .creationDate(LocalDate.now())
                .parentDepartment(null)
                .build();
    }

    private DepartmentDto getDepartmentDto(final Integer id, final String name) {
        return new DepartmentDto(
                id,
                name,
                LocalDate.now(),
                null
        );
    }

}
