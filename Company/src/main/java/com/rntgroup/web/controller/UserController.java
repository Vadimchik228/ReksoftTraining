package com.rntgroup.web.controller;

import com.rntgroup.service.UserService;
import com.rntgroup.web.dto.user.UserDto;
import com.rntgroup.web.dto.validation.group.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Produces;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Controller", description = "User API")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    @PreAuthorize("@cse.canAccessUser(#id)")
    public UserDto getById(@PathVariable final Long id) {
        return userService.getById(id);
    }

    @PutMapping
    @Operation(summary = "Update user")
    @PreAuthorize("@cse.canAccessUser(#dto.id)")
    public UserDto update(@Validated(OnUpdate.class) @RequestBody final UserDto dto) {
        return userService.update(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    @PreAuthorize("@cse.hasAdminRights()")
    public void delete(@PathVariable final Long id) {
        userService.delete(id);
    }
}
