package com.rntgroup.service;

import com.rntgroup.web.dto.user.UserDto;

public interface UserService {
    UserDto getById(Long id);

    UserDto update(UserDto dto);

    UserDto create(UserDto dto);

    void delete(Long id);
}
