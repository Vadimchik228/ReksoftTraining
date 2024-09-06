package com.rntgroup.integration.service;

import com.rntgroup.database.repository.UserRepository;
import com.rntgroup.exception.InvalidDataException;
import com.rntgroup.exception.ResourceNotFoundException;
import com.rntgroup.integration.IntegrationTestBase;
import com.rntgroup.service.UserService;
import com.rntgroup.web.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@RequiredArgsConstructor
public class UserServiceIT extends IntegrationTestBase {
    private static final long USER_ID = 1;
    private static final long NON_EXISTENT_USER_ID = 100;

    private final UserService userService;
    private final UserRepository userRepository;

    @Test
    void getById() {
        var dto = userService.getById(USER_ID);
        assertAll(
                () -> assertThat(dto.getId()).isEqualTo(USER_ID),
                () -> assertThat(dto.getName()).isEqualTo(
                        "Vadim Schebetovskiy"
                )
        );
    }

    @Test
    void getById_notFound() {
        assertThatThrownBy(() -> userService.getById(NON_EXISTENT_USER_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find user with id " +
                        NON_EXISTENT_USER_ID + "."
                );
    }

    @Test
    void update() {
        var dto = getUserDto(USER_ID);
        dto.setName("Vlad");

        var updatedDto = userService.update(dto);
        assertThat(updatedDto.getId()).isEqualTo(USER_ID);
        assertThat(updatedDto.getName()).isEqualTo("Vlad");

        var updatedEntity = userRepository.findById(USER_ID);
        assertThat(updatedEntity).isPresent();
        assertThat(updatedEntity.get().getName()).isEqualTo("Vlad");
    }

    @Test
    void update_notFound() {
        var dto = getUserDto(NON_EXISTENT_USER_ID);

        assertThatThrownBy(() -> userService.update(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find user with id " +
                        NON_EXISTENT_USER_ID + "."
                );
    }

    @Test
    void update_duplicateUsername() {
        var dto = getUserDto(USER_ID);
        dto.setUsername("mikewazowski@yahoo.com");

        assertThatThrownBy(() -> userService.update(dto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "There is already user with username" +
                        " mikewazowski@yahoo.com."
                );
    }

    @Test
    void create() {
        var dto = getUserDto(null);
        dto.setUsername("some@gmail.com");

        var createdDto = userService.create(dto);
        assertThat(createdDto.getId()).isNotNull();
        assertThat(createdDto.getUsername()).isEqualTo(
                "some@gmail.com"
        );

        var createdEntity = userRepository.findById(createdDto.getId());
        assertThat(createdEntity).isPresent();
        assertThat(createdEntity.get().getUsername()).isEqualTo(
                "some@gmail.com"
        );
    }

    @Test
    void create_duplicateUsername() {
        var dto = getUserDto(null);

        assertThatThrownBy(() -> userService.create(dto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "There is already user with username" +
                        " schebetovskiyvadim@gmail.com."
                );
    }

    @Test
    void delete() {
        userService.delete(USER_ID);
        var deletedEntity = userRepository.findById(USER_ID);
        assertThat(deletedEntity).isEmpty();
    }

    @Test
    void delete_notFound() {
        assertThatThrownBy(() -> userService.delete(NON_EXISTENT_USER_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find user with id " +
                        NON_EXISTENT_USER_ID + "."
                );
    }

    private UserDto getUserDto(final Long id) {
        return new UserDto(id,
                "Vadim Schebetovskiy",
                "schebetovskiyvadim@gmail.com",
                "123",
                "123");
    }
}