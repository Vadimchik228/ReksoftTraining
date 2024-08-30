package com.rntgroup.web.mapper;

import com.rntgroup.database.entity.Employee;
import com.rntgroup.web.dto.EmployeeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface EmployeeMapper extends Mappable<Employee, EmployeeDto> {
    @Mappings({
            @Mapping(target = "departmentId", source = "department.id"),
            @Mapping(target = "positionId", source = "position.id")
    })
    EmployeeDto toDto(Employee entity);

    @Mappings({
            @Mapping(target = "department", ignore = true),
            @Mapping(target = "position", ignore = true)
    })
    Employee toEntity(EmployeeDto dto);
}
