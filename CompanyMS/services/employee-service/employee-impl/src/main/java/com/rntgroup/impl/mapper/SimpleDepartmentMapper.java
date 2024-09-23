package com.rntgroup.impl.mapper;

import com.rntgroup.impl.entity.DepartmentSnapshot;
import com.rntroup.api.dto.SimpleDepartmentDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SimpleDepartmentMapper extends Mappable<DepartmentSnapshot, SimpleDepartmentDto> {
}
