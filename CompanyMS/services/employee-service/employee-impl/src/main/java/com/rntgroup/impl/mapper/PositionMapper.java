package com.rntgroup.impl.mapper;

import com.rntgroup.impl.entity.Position;
import com.rntroup.api.dto.PositionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PositionMapper extends Mappable<Position, PositionDto> {
}
