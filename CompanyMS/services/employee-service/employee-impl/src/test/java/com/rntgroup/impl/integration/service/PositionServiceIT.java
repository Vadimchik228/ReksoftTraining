package com.rntgroup.impl.integration.service;

import com.rntgroup.impl.integration.IntegrationTestBase;
import com.rntgroup.impl.repository.PositionRepository;
import com.rntgroup.impl.service.PositionService;
import com.rntroup.api.dto.PositionDto;
import com.rntroup.api.exception.InvalidDataException;
import com.rntroup.api.exception.InvalidDeletionException;
import com.rntroup.api.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@RequiredArgsConstructor
class PositionServiceIT extends IntegrationTestBase {
    private static final int POSITION_ID = 1;
    private static final int DEPENDENT_POSITION_ID = 2;
    private static final int INDEPENDENT_POSITION_ID = 11;
    private static final int NON_EXISTENT_POSITION_ID = 100;

    private final PositionService positionService;
    private final PositionRepository positionRepository;

    @Test
    void getById() {
        var dto = positionService.getById(POSITION_ID);
        assertAll(
                () -> assertThat(dto.getId()).isEqualTo(POSITION_ID),
                () -> assertThat(dto.getName()).isEqualTo(
                        "Финансовый директор (CFO)")
        );
    }

    @Test
    void getById_notFound() {
        assertThatThrownBy(() -> positionService.getById(NON_EXISTENT_POSITION_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find position with id " +
                        NON_EXISTENT_POSITION_ID + ".");
    }

    @Test
    void getAll() {
        var pageable = PageRequest.of(0, 2,
                Sort.by(Sort.Direction.ASC, "id"));
        var page = positionService.getAll(pageable);

        assertThat(page.getTotalElements()).isEqualTo(11);
        assertThat(page.getTotalPages()).isEqualTo(6);
        assertThat(page.getContent().get(0).getName()).isEqualTo(
                "Финансовый директор (CFO)");
        assertThat(page.getContent().get(1).getName()).isEqualTo(
                "Главный бухгалтер");
    }

    @Test
    void update() {
        var dto = new PositionDto(POSITION_ID, "Обновленное название");

        var updatedDto = positionService.update(dto);
        assertThat(updatedDto.getId()).isEqualTo(POSITION_ID);
        assertThat(updatedDto.getName()).isEqualTo(
                "Обновленное название");

        var updatedEntity = positionRepository.findById(POSITION_ID);
        assertThat(updatedEntity).isPresent();
        assertThat(updatedEntity.get().getName()).isEqualTo(
                "Обновленное название");
    }

    @Test
    void update_notFound() {
        var dto = new PositionDto(NON_EXISTENT_POSITION_ID,
                "Какая-то неведомая должность");

        assertThatThrownBy(() -> positionService.update(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find position with id " +
                        NON_EXISTENT_POSITION_ID + ".");
    }

    @Test
    void update_duplicateName() {
        var dto = new PositionDto(POSITION_ID, "Менеджер по закупкам");

        assertThatThrownBy(() -> positionService.update(dto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "There is already position with name" +
                        " Менеджер по закупкам.");
    }

    @Test
    void create() {
        var dto = new PositionDto(null, "Новая должность");

        var createdDto = positionService.create(dto);
        assertThat(createdDto.getId()).isNotNull();
        assertThat(createdDto.getName()).isEqualTo("Новая должность");

        var createdEntity = positionRepository.findById(createdDto.getId());
        assertThat(createdEntity).isPresent();
        assertThat(createdEntity.get().getName()).isEqualTo("Новая должность");
    }

    @Test
    void create_duplicateName() {
        var dto = new PositionDto();
        dto.setName("Финансовый директор (CFO)");

        assertThatThrownBy(() -> positionService.create(dto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "There is already position with name" +
                        " Финансовый директор (CFO).");
    }

    @Test
    void delete() {
        positionService.delete(INDEPENDENT_POSITION_ID);
        var deletedEntity = positionRepository.findById(INDEPENDENT_POSITION_ID);
        assertThat(deletedEntity).isEmpty();
    }

    @Test
    void delete_notFound() {
        assertThatThrownBy(() -> positionService.delete(NON_EXISTENT_POSITION_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find position with id " +
                        NON_EXISTENT_POSITION_ID + ".");
    }

    @Test
    void delete_withDependentEmployees() {
        assertThatThrownBy(() -> positionService.delete(DEPENDENT_POSITION_ID))
                .isInstanceOf(InvalidDeletionException.class)
                .hasMessageContaining(
                        "Couldn't delete position with id " +
                        DEPENDENT_POSITION_ID + "." +
                        " There are dependent employees in the DB.");
    }
}
