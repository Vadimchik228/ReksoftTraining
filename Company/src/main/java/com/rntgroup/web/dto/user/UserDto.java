package com.rntgroup.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rntgroup.web.dto.validation.annotation.CorrectPassword;
import com.rntgroup.web.dto.validation.annotation.MatchingPasswords;
import com.rntgroup.web.dto.validation.group.OnCreate;
import com.rntgroup.web.dto.validation.group.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User DTO")
@MatchingPasswords(
        groups = {OnCreate.class, OnUpdate.class}
)
public class UserDto {

    @Null(
            message = "Id must be null.",
            groups = OnCreate.class
    )
    @NotNull(
            message = "Id must be not null.",
            groups = OnUpdate.class
    )
    private Long id;

    @NotNull(
            message = "Name must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Size(
            message = "Name must consist of no more than 255 characters.",
            max = 255,
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String name;

    @NotNull(
            message = "Username must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Email(
            message = "Username must be a valid email address"
    )
    @Size(
            message = "Username must consist of no more than 255 characters.",
            max = 255,
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String username;

    @JsonProperty(
            access = JsonProperty.Access.WRITE_ONLY
    )
    @NotNull(
            message = "Password must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Size(
            message = "Password must consist of no more than 255 characters.",
            max = 255,
            groups = {OnCreate.class, OnUpdate.class}
    )
    @CorrectPassword(
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String password;

    @JsonProperty(
            access = JsonProperty.Access.WRITE_ONLY
    )
    @NotNull(
            message = "Password confirmation must be not null.",
            groups = {OnCreate.class}
    )
    @NotBlank(
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String passwordConfirmation;

}
