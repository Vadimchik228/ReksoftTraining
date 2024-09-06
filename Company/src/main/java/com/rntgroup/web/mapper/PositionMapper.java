package com.rntgroup.web.mapper;

import com.rntgroup.database.entity.Position;
import com.rntgroup.web.dto.position.PositionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PositionMapper extends Mappable<Position, PositionDto> {
}
