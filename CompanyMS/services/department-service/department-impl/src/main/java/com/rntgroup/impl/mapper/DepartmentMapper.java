package com.rntgroup.impl.mapper;

import com.rntgroup.api.dto.DepartmentDto;
import com.rntgroup.impl.entity.Department;
import lombok.Generated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Generated
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
