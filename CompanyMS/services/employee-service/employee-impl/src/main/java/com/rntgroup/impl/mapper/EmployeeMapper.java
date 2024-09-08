package com.rntgroup.impl.mapper;

import com.rntgroup.impl.entity.Employee;
import com.rntroup.api.dto.EmployeeDto;
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
            @Mapping(target = "positionId", source = "position.id")
    })
    EmployeeDto toDto(Employee entity);

    @Mappings({
            @Mapping(target = "position", ignore = true)
    })
    Employee toEntity(EmployeeDto dto);
}
