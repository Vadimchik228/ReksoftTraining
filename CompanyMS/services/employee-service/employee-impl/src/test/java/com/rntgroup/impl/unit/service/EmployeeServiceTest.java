package com.rntgroup.impl.unit.service;

import com.rntgroup.impl.entity.Employee;
import com.rntgroup.impl.entity.Position;
import com.rntgroup.impl.entity.Sex;
import com.rntgroup.impl.mapper.EmployeeMapper;
import com.rntgroup.impl.repository.DepartmentSnapshotRepository;
import com.rntgroup.impl.repository.EmployeeRepository;
import com.rntgroup.impl.repository.PositionRepository;
import com.rntgroup.impl.service.impl.EmployeeServiceImpl;
import com.rntroup.api.client.DepartmentClient;
import com.rntroup.api.dto.EmployeeDto;
import com.rntroup.api.exception.FeignClientNotFoundException;
import com.rntroup.api.exception.InvalidDataException;
import com.rntroup.api.exception.ResourceNotFoundException;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    private static final long FIRST_DIRECTOR_ID = 1L;
    private static final String FIRST_DIRECTOR_SALARY = "150000.00";
    private static final int FIRST_DEPARTMENT_ID = 1;
    private static final int FIRST_POSITION_ID = 1;
    private static final long EMPLOYEE_ID = 2L;
    private static final long NON_EXISTENT_EMPLOYEE_ID = 100L;
    private static final int NON_EXISTENT_POSITION_ID = 100;
    private static final int NON_EXISTENT_DEPARTMENT_ID = 100;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private DepartmentSnapshotRepository departmentSnapshotRepository;

    @Mock
    private DepartmentClient departmentClient;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void getById() {
        var employee = getEmployee(EMPLOYEE_ID);
        var employeeDto = getEmployeeDto(EMPLOYEE_ID);

        when(employeeRepository.findById(EMPLOYEE_ID))
                .thenReturn(Optional.of(employee));
        when(employeeMapper.toDto(employee))
                .thenReturn(employeeDto);

        var employeeDtoResult = employeeService
                .getById(EMPLOYEE_ID);

        assertThat(employeeDtoResult)
                .isEqualTo(employeeDto);
        verify(employeeRepository)
                .findById(EMPLOYEE_ID);
        verify(employeeMapper).toDto(employee);
    }

    @Test
    void getById_notFound() {
        when(employeeRepository.findById(NON_EXISTENT_EMPLOYEE_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getById(NON_EXISTENT_EMPLOYEE_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find employee with id " +
                        NON_EXISTENT_EMPLOYEE_ID + "."
                );

        verify(employeeRepository)
                .findById(NON_EXISTENT_EMPLOYEE_ID);
    }

    @Test
    void getDirectorByDepartmentId() {
        var employee = getEmployee(FIRST_DIRECTOR_ID);
        employee.setIsDirector(true);
        var employeeDto = getEmployeeDto(FIRST_DIRECTOR_ID);
        employeeDto.setIsDirector(true);

        when(employeeRepository.findDirectorByDepartmentId(FIRST_DEPARTMENT_ID))
                .thenReturn(Optional.of(employee));
        when(employeeMapper.toDto(employee))
                .thenReturn(employeeDto);
        when(departmentSnapshotRepository.existsById(FIRST_DEPARTMENT_ID))
                .thenReturn(true);

        var resultDto = employeeService
                .getDirectorByDepartmentId(FIRST_DEPARTMENT_ID);

        assertThat(resultDto).isPresent();
        assertThat(resultDto.get()).isEqualTo(employeeDto);
        verify(employeeRepository)
                .findDirectorByDepartmentId(FIRST_DEPARTMENT_ID);
        verify(employeeMapper)
                .toDto(employee);
        verify(departmentSnapshotRepository)
                .existsById(FIRST_DEPARTMENT_ID);
    }

    @Test
    void getDirectorByDepartmentId_notFound() {
        int departmentId = FIRST_DEPARTMENT_ID;
        when(employeeRepository
                .findDirectorByDepartmentId(departmentId))
                .thenReturn(Optional.empty());
        when(departmentSnapshotRepository.existsById(departmentId))
                .thenReturn(true);

        var directorDto = employeeService
                .getDirectorByDepartmentId(departmentId);

        assertThat(directorDto)
                .isEmpty();
        verify(employeeRepository)
                .findDirectorByDepartmentId(departmentId);
        verify(departmentSnapshotRepository)
                .existsById(departmentId);
    }

    @Test
    void getAllByDepartmentId() {
        var pageable = PageRequest.of(0, 4,
                Sort.by(Sort.Direction.DESC, "isDirector"));

        var director = getEmployee(FIRST_DIRECTOR_ID);
        director.setIsDirector(true);
        var employee2 = getEmployee(2L);
        var employee3 = getEmployee(3L);
        var employee4 = getEmployee(4L);

        var directorDto = getEmployeeDto(FIRST_DIRECTOR_ID);
        directorDto.setIsDirector(true);
        var employeeDto2 = getEmployeeDto(2L);
        var employeeDto3 = getEmployeeDto(3L);
        var employeeDto4 = getEmployeeDto(4L);

        var employees = List.of(director, employee2, employee3, employee4);
        var page = new PageImpl<>(employees, pageable, employees.size());

        when(employeeRepository.findAllByDepartmentId(FIRST_DEPARTMENT_ID, pageable))
                .thenReturn(page);
        when(employeeMapper.toDto(director)).thenReturn(directorDto);
        when(employeeMapper.toDto(employee2)).thenReturn(employeeDto2);
        when(employeeMapper.toDto(employee3)).thenReturn(employeeDto3);
        when(employeeMapper.toDto(employee4)).thenReturn(employeeDto4);

        var resultPage = employeeService
                .getAllByDepartmentId(FIRST_DEPARTMENT_ID, pageable);

        assertThat(resultPage.getTotalElements())
                .isEqualTo(4L);
        assertThat(resultPage.getTotalPages())
                .isEqualTo(1);
        assertThat(resultPage.getContent().get(0).getId())
                .isEqualTo(FIRST_DIRECTOR_ID);
        verify(employeeRepository)
                .findAllByDepartmentId(FIRST_DEPARTMENT_ID, pageable);
    }

    @Test
    void update() {
        var employeeDto = getEmployeeDto(EMPLOYEE_ID);
        employeeDto.setFirstName("Обновленное имя");
        var employee = getEmployee(EMPLOYEE_ID);
        employee.setFirstName("Обновленное имя");

        when(employeeRepository.existsById(EMPLOYEE_ID))
                .thenReturn(true);
        when(positionRepository.findById(FIRST_POSITION_ID))
                .thenReturn(Optional.of(
                        new Position(FIRST_POSITION_ID, "Финансовый директор")
                ));
        when(departmentSnapshotRepository.existsById(FIRST_DEPARTMENT_ID))
                .thenReturn(true);
        when(employeeRepository.findDirectorByDepartmentId(FIRST_DEPARTMENT_ID))
                .thenReturn(Optional.empty()); // Нет директора
        when(employeeMapper.toEntity(employeeDto))
                .thenReturn(employee);
        when(employeeRepository.saveAndFlush(employee))
                .thenReturn(employee);

        var updatedEmployeeDto = employeeService.update(employeeDto);

        assertThat(updatedEmployeeDto.getFirstName())
                .isEqualTo("Обновленное имя");
        verify(employeeRepository, times(1))
                .existsById(EMPLOYEE_ID);
        verify(positionRepository, times(1))
                .findById(1);
        verify(departmentSnapshotRepository, times(1))
                .existsById(FIRST_DEPARTMENT_ID);
        verify(employeeRepository, times(1))
                .findDirectorByDepartmentId(FIRST_DEPARTMENT_ID);
        verify(employeeMapper, times(1))
                .toEntity(employeeDto);
        verify(employeeRepository, times(1))
                .saveAndFlush(employee);
    }

    @Test
    void update_notFound() {
        var employeeDto = getEmployeeDto(NON_EXISTENT_EMPLOYEE_ID);

        when(employeeRepository.existsById(NON_EXISTENT_EMPLOYEE_ID))
                .thenReturn(false);

        assertThatThrownBy(() -> employeeService.update(employeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find employee with id " +
                        NON_EXISTENT_EMPLOYEE_ID + "."
                );

        verify(employeeRepository, never())
                .saveAndFlush(any());
        verify(employeeRepository, never())
                .findDirectorByDepartmentId(anyInt());
        verify(employeeMapper, never())
                .toEntity(any());
        verify(positionRepository, never())
                .findById(anyInt());
        verify(departmentSnapshotRepository, never())
                .existsById(anyInt());
    }

    @Test
    void update_invalidPosition() {
        var employeeDto = getEmployeeDto(EMPLOYEE_ID);
        employeeDto.setPositionId(NON_EXISTENT_POSITION_ID);

        when(employeeRepository.existsById(EMPLOYEE_ID))
                .thenReturn(true);
        when(positionRepository
                .findById(NON_EXISTENT_POSITION_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.update(employeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find position with id " +
                        NON_EXISTENT_POSITION_ID + "."
                );

        verify(employeeRepository, never())
                .saveAndFlush(any());
        verify(employeeRepository, never())
                .findDirectorByDepartmentId(anyInt());
        verify(employeeMapper, never())
                .toEntity(any());
        verify(departmentSnapshotRepository, never())
                .existsById(any());
    }


    @Test
    void update_invalidDepartment() {
        var employeeDto = getEmployeeDto(EMPLOYEE_ID);
        employeeDto.setDepartmentId(NON_EXISTENT_DEPARTMENT_ID);

        when(employeeRepository.existsById(EMPLOYEE_ID))
                .thenReturn(true);
        when(positionRepository
                .findById(FIRST_POSITION_ID))
                .thenReturn(Optional.of(getPosition()));
        when(departmentSnapshotRepository
                .existsById(NON_EXISTENT_DEPARTMENT_ID))
                .thenReturn(false);
        when(departmentClient
                .getById(NON_EXISTENT_DEPARTMENT_ID))
                .thenThrow(FeignException.class);

        assertThatThrownBy(() -> employeeService.update(employeeDto))
                .isInstanceOf(FeignClientNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find department with id "
                        + NON_EXISTENT_DEPARTMENT_ID + "."
                );

        verify(employeeRepository, never())
                .saveAndFlush(any());
        verify(employeeRepository, never())
                .findDirectorByDepartmentId(anyInt());
        verify(employeeMapper, never())
                .toEntity(any());
        verify(positionRepository, times(1))
                .findById(FIRST_POSITION_ID);
        verify(departmentSnapshotRepository, times(1))
                .existsById(NON_EXISTENT_DEPARTMENT_ID);
        verify(departmentClient, times(1))
                .getById(NON_EXISTENT_DEPARTMENT_ID);
    }

    @Test
    void update_setDirector_alreadyDirector() {
        var employeeDto = getEmployeeDto(EMPLOYEE_ID);
        employeeDto.setDepartmentId(FIRST_DEPARTMENT_ID);
        employeeDto.setIsDirector(true);

        var director = getEmployee(FIRST_DIRECTOR_ID);
        director.setDepartmentId(FIRST_DEPARTMENT_ID);
        director.setIsDirector(true);

        when(employeeRepository.existsById(EMPLOYEE_ID))
                .thenReturn(true);
        when(positionRepository
                .findById(FIRST_POSITION_ID))
                .thenReturn(Optional.of(getPosition()));
        when(departmentSnapshotRepository
                .existsById(FIRST_DEPARTMENT_ID))
                .thenReturn(true);
        when(employeeRepository
                .findDirectorByDepartmentId(FIRST_DEPARTMENT_ID))
                .thenReturn(Optional.of(director));

        assertThatThrownBy(() -> employeeService.update(employeeDto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "The department with id " +
                        FIRST_DEPARTMENT_ID + " already has a director."
                );

        verify(employeeRepository, never())
                .saveAndFlush(any());
        verify(employeeMapper, never())
                .toEntity(any());
    }


    @Test
    void update_salaryHigherThanDirector() {
        var employeeDto = getEmployeeDto(EMPLOYEE_ID);
        employeeDto.setDepartmentId(FIRST_DEPARTMENT_ID);
        employeeDto.setSalary(BigDecimal.valueOf(10000000));

        var director = getEmployee(FIRST_DIRECTOR_ID);
        director.setDepartmentId(FIRST_DEPARTMENT_ID);
        director.setIsDirector(true);
        director.setSalary(new BigDecimal(FIRST_DIRECTOR_SALARY));

        when(employeeRepository.existsById(EMPLOYEE_ID))
                .thenReturn(true);
        when(positionRepository
                .findById(FIRST_POSITION_ID))
                .thenReturn(Optional.of(getPosition()));
        when(departmentSnapshotRepository
                .existsById(FIRST_DEPARTMENT_ID))
                .thenReturn(true);
        when(employeeRepository
                .findDirectorByDepartmentId(FIRST_DEPARTMENT_ID))
                .thenReturn(Optional.of(director));

        assertThatThrownBy(() -> employeeService.update(employeeDto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "The employee's salary cannot be " +
                        "higher than the director's salary "
                        + FIRST_DIRECTOR_SALARY + "."
                );

        verify(employeeRepository, never())
                .saveAndFlush(any());
        verify(employeeMapper, never())
                .toEntity(any());
    }

    @Test
    void update_salaryLowerThanMaxSalaryOfNonDirector() {
        var employeeDto = getEmployeeDto(EMPLOYEE_ID);
        employeeDto.setDepartmentId(FIRST_DEPARTMENT_ID);
        employeeDto.setIsDirector(true);

        when(employeeRepository.existsById(EMPLOYEE_ID))
                .thenReturn(true);
        when(positionRepository
                .findById(FIRST_POSITION_ID))
                .thenReturn(Optional.of(getPosition()));
        when(departmentSnapshotRepository
                .existsById(FIRST_DEPARTMENT_ID))
                .thenReturn(true);
        when(employeeRepository
                .findDirectorByDepartmentId(FIRST_DEPARTMENT_ID))
                .thenReturn(Optional.empty());
        when(employeeRepository
                .findMaxSalaryOfNonDirectorByDepartmentId(FIRST_DEPARTMENT_ID))
                .thenReturn(new BigDecimal("1000000000"));

        assertThatThrownBy(() -> employeeService.update(employeeDto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "The director's salary cannot be lower " +
                        "than the employer's salary 1000000000."
                );

        verify(employeeRepository, never())
                .saveAndFlush(any());
        verify(employeeMapper, never())
                .toEntity(any());
    }

    @Test
    void create() {
        var employeeDto = getEmployeeDto(null);
        var employee = getEmployee(null);
        var createdEmployee = getEmployee(EMPLOYEE_ID);

        when(positionRepository.findById(FIRST_POSITION_ID))
                .thenReturn(Optional.of(getPosition()));
        when(departmentSnapshotRepository
                .existsById(FIRST_DEPARTMENT_ID))
                .thenReturn(true);
        when(employeeRepository
                .findDirectorByDepartmentId(FIRST_DEPARTMENT_ID))
                .thenReturn(Optional.empty());
        when(employeeMapper.toEntity(employeeDto))
                .thenReturn(employee);
        when(employeeRepository.saveAndFlush(employee))
                .thenReturn(createdEmployee);

        var createdEmployeeDto = employeeService.create(employeeDto);

        assertThat(createdEmployeeDto.getId())
                .isEqualTo(EMPLOYEE_ID);
        assertThat(createdEmployeeDto.getFirstName())
                .isEqualTo(employeeDto.getFirstName());
        assertThat(createdEmployeeDto.getLastName())
                .isEqualTo(employeeDto.getLastName());
        verify(positionRepository, times(1))
                .findById(1);
        verify(departmentSnapshotRepository, times(1))
                .existsById(FIRST_DEPARTMENT_ID);
        verify(employeeRepository, times(1))
                .findDirectorByDepartmentId(FIRST_DEPARTMENT_ID);
        verify(employeeMapper, times(1))
                .toEntity(employeeDto);
        verify(employeeRepository, times(1))
                .saveAndFlush(employee);
    }

    @Test
    void create_invalidPosition() {
        var employeeDto = getEmployeeDto(null);
        employeeDto.setPositionId(NON_EXISTENT_POSITION_ID);

        when(positionRepository
                .findById(NON_EXISTENT_POSITION_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.create(employeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find position with id " +
                        NON_EXISTENT_POSITION_ID + "."
                );

        verify(employeeRepository, never())
                .saveAndFlush(any());
        verify(employeeRepository, never())
                .findDirectorByDepartmentId(anyInt());
        verify(employeeMapper, never())
                .toEntity(any());
        verify(departmentSnapshotRepository, never())
                .existsById(any());
    }

    @Test
    void create_invalidDepartment() {
        var employeeDto = getEmployeeDto(null);
        employeeDto.setDepartmentId(NON_EXISTENT_DEPARTMENT_ID);

        when(positionRepository
                .findById(FIRST_POSITION_ID))
                .thenReturn(Optional.of(getPosition()));
        when(departmentSnapshotRepository
                .existsById(NON_EXISTENT_DEPARTMENT_ID))
                .thenReturn(false);
        when(departmentClient
                .getById(NON_EXISTENT_DEPARTMENT_ID))
                .thenThrow(FeignException.class);

        assertThatThrownBy(() -> employeeService.create(employeeDto))
                .isInstanceOf(FeignClientNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find department with id "
                        + NON_EXISTENT_DEPARTMENT_ID + "."
                );

        verify(employeeRepository, never())
                .saveAndFlush(any());
        verify(employeeRepository, never())
                .findDirectorByDepartmentId(anyInt());
        verify(employeeMapper, never())
                .toEntity(any());
        verify(positionRepository, times(1))
                .findById(FIRST_POSITION_ID);
        verify(departmentSnapshotRepository, times(1))
                .existsById(NON_EXISTENT_DEPARTMENT_ID);
        verify(departmentClient, times(1))
                .getById(NON_EXISTENT_DEPARTMENT_ID);
    }

    @Test
    void create_setDirector_alreadyDirector() {
        var employeeDto = getEmployeeDto(null);
        employeeDto.setDepartmentId(FIRST_DEPARTMENT_ID);
        employeeDto.setIsDirector(true);

        var director = getEmployee(FIRST_DIRECTOR_ID);
        director.setDepartmentId(FIRST_DEPARTMENT_ID);
        director.setIsDirector(true);

        when(positionRepository
                .findById(FIRST_POSITION_ID))
                .thenReturn(Optional.of(getPosition()));
        when(departmentSnapshotRepository
                .existsById(FIRST_DEPARTMENT_ID))
                .thenReturn(true);
        when(employeeRepository
                .findDirectorByDepartmentId(FIRST_DEPARTMENT_ID))
                .thenReturn(Optional.of(director));

        assertThatThrownBy(() -> employeeService.create(employeeDto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "The department with id " +
                        FIRST_DEPARTMENT_ID + " already has a director."
                );

        verify(employeeRepository, never())
                .saveAndFlush(any());
        verify(employeeMapper, never())
                .toEntity(any());
    }


    @Test
    void create_salaryHigherThanDirector() {
        var employeeDto = getEmployeeDto(null);
        employeeDto.setDepartmentId(FIRST_DEPARTMENT_ID);
        employeeDto.setSalary(BigDecimal.valueOf(10000000));

        var director = getEmployee(FIRST_DIRECTOR_ID);
        director.setDepartmentId(FIRST_DEPARTMENT_ID);
        director.setIsDirector(true);
        director.setSalary(new BigDecimal(FIRST_DIRECTOR_SALARY));

        when(positionRepository
                .findById(FIRST_POSITION_ID))
                .thenReturn(Optional.of(getPosition()));
        when(departmentSnapshotRepository
                .existsById(FIRST_DEPARTMENT_ID))
                .thenReturn(true);
        when(employeeRepository
                .findDirectorByDepartmentId(FIRST_DEPARTMENT_ID))
                .thenReturn(Optional.of(director));

        assertThatThrownBy(() -> employeeService.create(employeeDto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "The employee's salary cannot be " +
                        "higher than the director's salary "
                        + FIRST_DIRECTOR_SALARY + "."
                );

        verify(employeeRepository, never())
                .saveAndFlush(any());
        verify(employeeMapper, never())
                .toEntity(any());
    }

    @Test
    void create_salaryLowerThanMaxSalaryOfNonDirector() {
        var employeeDto = getEmployeeDto(null);
        employeeDto.setDepartmentId(FIRST_DEPARTMENT_ID);
        employeeDto.setIsDirector(true);

        when(positionRepository
                .findById(FIRST_POSITION_ID))
                .thenReturn(Optional.of(getPosition()));
        when(departmentSnapshotRepository
                .existsById(FIRST_DEPARTMENT_ID))
                .thenReturn(true);
        when(employeeRepository
                .findDirectorByDepartmentId(FIRST_DEPARTMENT_ID))
                .thenReturn(Optional.empty());
        when(employeeRepository
                .findMaxSalaryOfNonDirectorByDepartmentId(FIRST_DEPARTMENT_ID))
                .thenReturn(new BigDecimal("1000000000"));

        assertThatThrownBy(() -> employeeService.create(employeeDto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "The director's salary cannot be lower " +
                        "than the employer's salary 1000000000."
                );

        verify(employeeRepository, never())
                .saveAndFlush(any());
        verify(employeeMapper, never())
                .toEntity(any());
    }

    @Test
    void delete() {
        when(employeeRepository.existsById(EMPLOYEE_ID))
                .thenReturn(true);

        employeeService.delete(EMPLOYEE_ID);

        verify(employeeRepository, times(1))
                .existsById(EMPLOYEE_ID);
        verify(employeeRepository, times(1))
                .deleteById(EMPLOYEE_ID);
    }

    @Test
    void delete_notFound() {
        when(employeeRepository.existsById(NON_EXISTENT_EMPLOYEE_ID))
                .thenReturn(false);

        assertThatThrownBy(() -> employeeService
                .delete(NON_EXISTENT_EMPLOYEE_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find employee with id " +
                        NON_EXISTENT_EMPLOYEE_ID + "."
                );

        verify(employeeRepository, times(1))
                .existsById(NON_EXISTENT_EMPLOYEE_ID);
        verify(employeeRepository, never())
                .deleteById(NON_EXISTENT_EMPLOYEE_ID);
    }

    @Test
    void countAllByDepartmentId() {
        when(employeeRepository
                .countAllByDepartmentId(FIRST_DEPARTMENT_ID))
                .thenReturn(6L);
        when(departmentSnapshotRepository
                .existsById(FIRST_DEPARTMENT_ID))
                .thenReturn(true);

        long employeesCount = employeeService
                .countAllByDepartmentId(FIRST_DEPARTMENT_ID);

        assertThat(employeesCount)
                .isEqualTo(6L);
        verify(employeeRepository)
                .countAllByDepartmentId(FIRST_DEPARTMENT_ID);
        verify(departmentSnapshotRepository)
                .existsById(FIRST_DEPARTMENT_ID);
    }

    @Test
    void countAllByDepartmentId_notFound() {
        when(departmentSnapshotRepository
                .existsById(NON_EXISTENT_DEPARTMENT_ID))
                .thenReturn(false);
        when(departmentClient
                .getById(NON_EXISTENT_DEPARTMENT_ID))
                .thenThrow(FeignException.class);

        assertThatThrownBy(() -> employeeService
                .countAllByDepartmentId(NON_EXISTENT_DEPARTMENT_ID))
                .isInstanceOf(FeignClientNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find department with id " +
                        NON_EXISTENT_DEPARTMENT_ID + "."
                );

        verify(departmentSnapshotRepository)
                .existsById(NON_EXISTENT_DEPARTMENT_ID);
        verify(employeeRepository, never())
                .countAllByDepartmentId(anyInt());
    }

    @Test
    void sumSalariesByDepartmentId() {
        when(employeeRepository
                .sumSalariesByDepartmentId(FIRST_DEPARTMENT_ID))
                .thenReturn(new BigDecimal("500000.00"));
        when(departmentSnapshotRepository
                .existsById(FIRST_DEPARTMENT_ID))
                .thenReturn(true);

        var salaryFund = employeeService
                .sumSalariesByDepartmentId(FIRST_DEPARTMENT_ID);

        assertThat(salaryFund)
                .isEqualTo(new BigDecimal("500000.00"));
        verify(employeeRepository)
                .sumSalariesByDepartmentId(FIRST_DEPARTMENT_ID);
        verify(departmentSnapshotRepository)
                .existsById(FIRST_DEPARTMENT_ID);
    }

    @Test
    void sumSalariesByDepartmentId_notFound() {
        when(departmentSnapshotRepository
                .existsById(NON_EXISTENT_DEPARTMENT_ID))
                .thenReturn(false);
        when(departmentClient
                .getById(NON_EXISTENT_DEPARTMENT_ID))
                .thenThrow(FeignException.class);

        assertThatThrownBy(() -> employeeService
                .sumSalariesByDepartmentId(NON_EXISTENT_DEPARTMENT_ID))
                .isInstanceOf(FeignClientNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find department with id " +
                        NON_EXISTENT_DEPARTMENT_ID + "."
                );

        verify(departmentSnapshotRepository)
                .existsById(NON_EXISTENT_DEPARTMENT_ID);
        verify(employeeRepository, never())
                .sumSalariesByDepartmentId(anyInt());
    }

    private Employee getEmployee(final Long id) {
        return Employee.builder()
                .id(id)
                .lastName("Новая фамилия")
                .firstName("Новое имя")
                .patronymic("Новое отчество")
                .sex(Sex.MALE)
                .birthDate(LocalDate.of(2003, 12, 12))
                .phoneNumber("+79507742275")
                .employmentDate(LocalDate.now())
                .dismissalDate(null)
                .position(getPosition())
                .salary(BigDecimal.valueOf(100000))
                .isDirector(false)
                .departmentId(FIRST_DEPARTMENT_ID)
                .build();
    }

    private EmployeeDto getEmployeeDto(final Long id) {
        return new EmployeeDto(
                id,
                "Новая фамилия",
                "Новое имя",
                "Новое отчество",
                "MALE",
                LocalDate.now(),
                "+79507742275",
                LocalDate.now(),
                null,
                FIRST_POSITION_ID,
                BigDecimal.valueOf(100000),
                false,
                FIRST_DEPARTMENT_ID
        );
    }

    private Position getPosition() {
        return Position.builder()
                .id(FIRST_POSITION_ID)
                .name("Финансовый директор")
                .build();
    }

}
