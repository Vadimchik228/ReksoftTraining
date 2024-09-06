package com.rntgroup.service.impl;

import com.rntgroup.database.entity.Role;
import com.rntgroup.database.entity.User;
import com.rntgroup.database.repository.UserRepository;
import com.rntgroup.exception.InvalidDataException;
import com.rntgroup.exception.ResourceNotFoundException;
import com.rntgroup.service.UserService;
import com.rntgroup.web.dto.user.UserDto;
import com.rntgroup.web.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(final Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                                "Couldn't find user with id " + id + "."
                        )
                );
    }

    @Override
    public UserDto update(final UserDto dto) {
        checkIfUserIdExists(dto.getId());

        try {
            var entity = userMapper.toEntity(dto);
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
            userRepository.saveAndFlush(entity);
            return dto;
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidDataException(
                    "There is already user with username " + dto.getUsername() + "."
            );
        }
    }

    @Override
    public UserDto create(UserDto dto) {
        try {
            User entity = userMapper.toEntity(dto);
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
            Set<Role> roles = Set.of(Role.USER);
            entity.setRoles(roles);
            entity = userRepository.saveAndFlush(entity);
            dto.setId(entity.getId());
            return dto;
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidDataException(
                    "There is already user with username " + dto.getUsername() + "."
            );
        }
    }

    @Override
    public void delete(final Long id) {
        checkIfUserIdExists(id);
        userRepository.deleteById(id);
    }

    private void checkIfUserIdExists(final Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "Couldn't find user with id " + userId + "."
            );
        }
    }
}
