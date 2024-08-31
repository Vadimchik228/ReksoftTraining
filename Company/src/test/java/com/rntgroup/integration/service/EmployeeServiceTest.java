package com.rntgroup.integration.service;

import com.rntgroup.database.entity.Sex;
import com.rntgroup.database.repository.EmployeeRepository;
import com.rntgroup.exception.InvalidDataException;
import com.rntgroup.exception.ResourceNotFoundException;
import com.rntgroup.integration.IntegrationTestBase;
import com.rntgroup.service.EmployeeService;
import com.rntgroup.web.dto.EmployeeDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@RequiredArgsConstructor
class EmployeeServiceTest extends IntegrationTestBase {
    private static final long FIRST_DIRECTOR_ID = 1L;
    private static final String FIRST_DIRECTOR_SALARY = "150000.00";
    private static final int FIRST_DEPARTMENT_ID = 1;
    private static final long EMPLOYEE_ID = 2L;
    private static final long NON_EXISTENT_EMPLOYEE_ID = 100L;
    private static final int NON_EXISTENT_POSITION_ID = 100;
    private static final int NON_EXISTENT_DEPARTMENT_ID = 100;


    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    @Test
    void getById() {
        var employeeDto = employeeService.getById(FIRST_DIRECTOR_ID);
        assertThat(employeeDto.getId()).isEqualTo(FIRST_DIRECTOR_ID);
        assertThat(employeeDto.getFirstName()).isEqualTo("Иван");
    }

    @Test
    void getById_notFound() {
        assertThatThrownBy(() -> employeeService.getById(NON_EXISTENT_EMPLOYEE_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find employee with id " +
                        NON_EXISTENT_EMPLOYEE_ID + ".");
    }

    @Test
    void getDirectorByDepartmentId() {
        var directorDto = employeeService.getDirectorByDepartmentId(
                FIRST_DEPARTMENT_ID);
        assertThat(directorDto).isPresent();
        assertThat(directorDto.get().getId()).isEqualTo(FIRST_DIRECTOR_ID);
    }

    @Test
    void getDirectorByDepartmentId_notFound() {
        var directorDto = employeeService.getDirectorByDepartmentId(5);
        assertThat(directorDto).isEmpty();
    }

    @Test
    void getAllByDepartmentId() {
        var pageable = PageRequest.of(0, 5,
                Sort.by(Sort.Direction.DESC, "isDirector"));
        var page = employeeService.getAllByDepartmentId(
                FIRST_DEPARTMENT_ID, pageable);

        assertThat(page.getTotalElements()).isEqualTo(6L);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getContent().get(0).getId()).isEqualTo(FIRST_DIRECTOR_ID);
    }

    @Test
    void update() {
        var dto = getEmployeeDto(FIRST_DIRECTOR_ID);

        var updatedDto = employeeService.update(dto);
        assertThat(updatedDto.getId()).isEqualTo(FIRST_DIRECTOR_ID);
        assertThat(updatedDto.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(updatedDto.getDepartmentId()).isEqualTo(dto.getDepartmentId());
        assertThat(updatedDto.getPositionId()).isEqualTo(dto.getPositionId());
        assertThat(updatedDto.getIsDirector()).isFalse();
        assertThat(updatedDto.getSalary()).isEqualTo(dto.getSalary());
    }

    @Test
    void update_notFound() {
        var dto = getEmployeeDto(NON_EXISTENT_EMPLOYEE_ID);

        assertThatThrownBy(() -> employeeService.update(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find employee with id " +
                        NON_EXISTENT_EMPLOYEE_ID + ".");
    }

    @Test
    void update_invalidPosition() {
        var dto = getEmployeeDto(FIRST_DIRECTOR_ID);
        dto.setPositionId(NON_EXISTENT_POSITION_ID);

        assertThatThrownBy(() -> employeeService.update(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find position with id " +
                        NON_EXISTENT_POSITION_ID + ".");
    }

    @Test
    void update_invalidDepartment() {
        var dto = getEmployeeDto(FIRST_DIRECTOR_ID);
        dto.setDepartmentId(NON_EXISTENT_DEPARTMENT_ID);

        assertThatThrownBy(() -> employeeService.update(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find department with id " +
                        NON_EXISTENT_DEPARTMENT_ID + ".");
    }

    @Test
    void update_setDirector_alreadyDirector() {
        var dto = getEmployeeDto(EMPLOYEE_ID);
        dto.setIsDirector(true);

        assertThatThrownBy(() -> employeeService.update(dto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "The department with id " + dto.getDepartmentId() +
                        " already has a director.");
    }

    @Test
    void update_salaryHigherThanDirector() {
        var dto = getEmployeeDto(EMPLOYEE_ID);
        dto.setSalary(new BigDecimal("10000000.00"));

        assertThatThrownBy(() -> employeeService.update(dto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("The employee's salary cannot be " +
                                      "higher than the director's salary " +
                                      FIRST_DIRECTOR_SALARY + ".");
    }

    @Test
    void update_salaryLowerThanMaxSalaryOfNonDirector() {
        var dto = getEmployeeDto(19L);
        dto.setDepartmentId(5);
        dto.setSalary(new BigDecimal("50000.00"));
        dto.setIsDirector(true);

        assertThatThrownBy(() -> employeeService.update(dto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "The director's salary cannot be lower " +
                        "than the employer's salary 130000.00.");
    }

    @Test
    void create() {
        var dto = getEmployeeDto(null);
        dto.setPositionId(2);

        var createdDto = employeeService.create(dto);
        assertThat(createdDto.getId()).isNotNull();
        assertThat(createdDto.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(createdDto.getLastName()).isEqualTo(dto.getLastName());
        assertThat(createdDto.getDepartmentId()).isEqualTo(dto.getDepartmentId());
        assertThat(createdDto.getPositionId()).isEqualTo(dto.getPositionId());
        assertThat(createdDto.getIsDirector()).isFalse();
        assertThat(createdDto.getSalary()).isEqualTo(dto.getSalary());
    }

    @Test
    void create_invalidPosition() {
        var dto = getEmployeeDto(null);
        dto.setPositionId(NON_EXISTENT_POSITION_ID);

        assertThatThrownBy(() -> employeeService.create(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find position with id " +
                        NON_EXISTENT_POSITION_ID + ".");
    }

    @Test
    void create_invalidDepartment() {
        var dto = getEmployeeDto(null);
        dto.setDepartmentId(NON_EXISTENT_DEPARTMENT_ID);

        assertThatThrownBy(() -> employeeService.create(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find department with id " +
                        NON_EXISTENT_DEPARTMENT_ID + ".");
    }

    @Test
    void create_setDirector_alreadyDirector() {
        var dto = getEmployeeDto(EMPLOYEE_ID);
        dto.setIsDirector(true);

        assertThatThrownBy(() -> employeeService.create(dto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "The department with id " + dto.getDepartmentId() +
                        " already has a director.");
    }

    @Test
    void create_salaryHigherThanDirector() {
        var dto = getEmployeeDto(EMPLOYEE_ID);
        dto.setSalary(new BigDecimal("10000000.00"));

        assertThatThrownBy(() -> employeeService.create(dto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("The employee's salary cannot be " +
                                      "higher than the director's salary "
                                      + FIRST_DIRECTOR_SALARY + ".");
    }

    @Test
    void create_salaryLowerThanMaxSalaryOfNonDirector() {
        var dto = getEmployeeDto(null);
        dto.setDepartmentId(5);
        dto.setSalary(new BigDecimal("50000.00"));
        dto.setIsDirector(true);

        assertThatThrownBy(() -> employeeService.create(dto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "The director's salary cannot be lower " +
                        "than the employer's salary 130000.00.");
    }

    @Test
    void delete() {
        employeeService.delete(EMPLOYEE_ID);

        var deletedEntity = employeeRepository.findById(EMPLOYEE_ID);
        assertThat(deletedEntity).isEmpty();
    }

    @Test
    void delete_notFound() {
        assertThatThrownBy(() -> employeeService.delete(NON_EXISTENT_EMPLOYEE_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find employee with id " +
                        NON_EXISTENT_EMPLOYEE_ID + ".");
    }

    @Test
    void countAllByDepartmentId() {
        Long count = employeeService.countAllByDepartmentId(FIRST_DEPARTMENT_ID);
        assertThat(count).isEqualTo(6L);
    }

    @Test
    void countAllByDepartmentId_notFound() {
        assertThatThrownBy(() -> employeeService
                .countAllByDepartmentId(NON_EXISTENT_DEPARTMENT_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find department with id " +
                        NON_EXISTENT_DEPARTMENT_ID + ".");
    }

    @Test
    void sumSalariesByDepartmentId() {
        BigDecimal sumSalaries = employeeService.sumSalariesByDepartmentId(
                FIRST_DEPARTMENT_ID);
        assertThat(sumSalaries).isEqualTo(new BigDecimal("515000.00"));
    }

    @Test
    void sumSalariesByDepartmentId_notFound() {
        assertThatThrownBy(() -> employeeService
                .sumSalariesByDepartmentId(
                        NON_EXISTENT_DEPARTMENT_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find department with id " +
                        NON_EXISTENT_DEPARTMENT_ID + ".");
    }

    private EmployeeDto getEmployeeDto(Long id) {
        return new EmployeeDto(
                id,
                "Новая фамилия",
                "Новое имя",
                "Новое отчество",
                Sex.MALE,
                LocalDate.now(),
                "+79507742275",
                LocalDate.now(),
                null,
                1,
                BigDecimal.valueOf(100000),
                false,
                1
        );
    }

}
