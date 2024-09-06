package com.rntgroup.web.mapper;

import com.rntgroup.database.entity.Department;
import com.rntgroup.web.dto.department.DepartmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DepartmentMapper extends Mappable<Department, DepartmentDto> {
    @Mappings({
            @Mapping(target = "parentDepartmentId", source = "parentDepartment.id")
    })
    DepartmentDto toDto(Department entity);

    @Mappings({
            @Mapping(target = "parentDepartment", ignore = true)
    })
    Department toEntity(DepartmentDto dto);
}
