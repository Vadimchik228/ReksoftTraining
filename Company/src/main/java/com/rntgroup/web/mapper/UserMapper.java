package com.rntgroup.web.mapper;

import com.rntgroup.database.entity.User;
import com.rntgroup.web.dto.user.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {
}
