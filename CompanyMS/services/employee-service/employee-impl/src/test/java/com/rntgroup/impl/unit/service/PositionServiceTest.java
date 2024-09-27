package com.rntgroup.impl.unit.service;

import com.rntgroup.impl.entity.Position;
import com.rntgroup.impl.mapper.PositionMapper;
import com.rntgroup.impl.repository.PositionRepository;
import com.rntgroup.impl.service.impl.PositionServiceImpl;
import com.rntroup.api.dto.PositionDto;
import com.rntroup.api.exception.InvalidDataException;
import com.rntroup.api.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PositionServiceTest {
    private static final int POSITION_ID = 1;
    private static final String POSITION_NAME =
            "Финансовый директор (CFO)";
    private static final int DEPENDENT_POSITION_ID = 2;
    private static final int INDEPENDENT_POSITION_ID = 11;
    private static final int NON_EXISTENT_POSITION_ID = 100;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private PositionMapper positionMapper;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private PositionServiceImpl positionService;

    @Test
    void getById() {
        var position = getPosition();
        var positionDto = getPositionDto();

        when(positionRepository.findById(POSITION_ID))
                .thenReturn(Optional.of(position));
        when(positionMapper.toDto(position))
                .thenReturn(positionDto);

        var result = positionService.getById(POSITION_ID);

        assertThat(result).isEqualTo(positionDto);
    }

    @Test
    void getById_notFound() {
        when(positionRepository.findById(NON_EXISTENT_POSITION_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> positionService.getById(NON_EXISTENT_POSITION_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find position with id "
                        + NON_EXISTENT_POSITION_ID + "."
                );
    }

    @Test
    void getAll() {
        var pageable = PageRequest.of(0, 4,
                Sort.by(Sort.Direction.ASC, "level", "name"));

        var position1 = new Position(1, "Финансовый директор (CFO)");
        var position2 = new Position(2, "Главный бухгалтер");
        var position3 = new Position(3, "Финансовый аналитик");
        var position4 = new Position(4, "Бухгалтер");

        var positionDto1 = new PositionDto(1, "Финансовый директор (CFO)");
        var positionDto2 = new PositionDto(2, "Главный бухгалтер");
        var positionDto3 = new PositionDto(3, "Финансовый аналитик");
        var positionDto4 = new PositionDto(4, "Бухгалтер");

        var positions = List.of(position1, position2, position3, position4);
        var page = new PageImpl<>(positions, pageable, positions.size());

        when(positionRepository.findAllBy(pageable))
                .thenReturn(page);
        when(positionMapper.toDto(position1)).thenReturn(positionDto1);
        when(positionMapper.toDto(position2)).thenReturn(positionDto2);
        when(positionMapper.toDto(position3)).thenReturn(positionDto3);
        when(positionMapper.toDto(position4)).thenReturn(positionDto4);

        var result = positionService.getAll(pageable);

        assertThat(result.getTotalElements())
                .isEqualTo(4);
        assertThat(result.getTotalPages())
                .isEqualTo(1);

        verify(positionRepository, times(1))
                .findAllBy(pageable);
        verify(positionMapper, times(4))
                .toDto(any());
    }

    @Test
    void update() {
        var positionDto = getPositionDto();
        positionDto.setName("Обновленная должность");
        var position = getPosition();
        position.setName("Обновленная должность");

        when(positionRepository.existsById(POSITION_ID))
                .thenReturn(true);
        when(positionRepository.saveAndFlush(any(Position.class)))
                .thenReturn(position);
        when(positionMapper.toEntity(positionDto))
                .thenReturn(position);

        var updatedPositionDto = positionService.update(positionDto);

        assertThat(updatedPositionDto.getId())
                .isEqualTo(POSITION_ID);
        assertThat(updatedPositionDto.getName())
                .isEqualTo("Обновленная должность");

        verify(positionRepository, times(1))
                .existsById(POSITION_ID);
        verify(positionRepository, times(1))
                .saveAndFlush(position);
        verify(positionMapper, times(1))
                .toEntity(positionDto);
    }

    @Test
    void update_notFound() {
        var positionDto = getPositionDto();
        positionDto.setId(NON_EXISTENT_POSITION_ID);

        when(positionRepository.existsById(NON_EXISTENT_POSITION_ID))
                .thenReturn(false);

        assertThatThrownBy(() -> positionService.update(positionDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find position with id " +
                        NON_EXISTENT_POSITION_ID + "."
                );

        verify(positionRepository, times(1))
                .existsById(NON_EXISTENT_POSITION_ID);
        verify(positionRepository, never()).saveAndFlush(any());
        verify(positionMapper, never()).toEntity(any());
    }

    @Test
    void update_duplicateName() {
        var positionDto = getPositionDto();
        positionDto.setId(2);

        when(positionRepository.existsById(2))
                .thenReturn(true);
        doThrow(DataIntegrityViolationException.class)
                .when(positionRepository).saveAndFlush(any());

        assertThatThrownBy(() -> positionService.update(positionDto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "There is already position with name "
                        + POSITION_NAME + "."
                );

        verify(positionRepository, times(1))
                .existsById(2);
        verify(positionRepository, times(1))
                .saveAndFlush(any());
        verify(positionMapper, times(1))
                .toEntity(positionDto);
    }

    @Test
    void create() {
        var positionDto = getPositionDto();
        positionDto.setId(null);
        var position = getPosition();
        positionDto.setId(null);

        when(positionRepository.saveAndFlush(any(Position.class)))
                .thenReturn(position);
        when(positionMapper.toEntity(positionDto))
                .thenReturn(position);

        var createdDepartmentDto = positionService.create(positionDto);

        assertThat(createdDepartmentDto.getId())
                .isEqualTo(position.getId());

        verify(positionRepository, times(1))
                .saveAndFlush(position);
        verify(positionMapper, times(1))
                .toEntity(positionDto);
    }

    @Test
    void create_duplicateName() {
        var positionDto = getPositionDto();
        positionDto.setId(null);
        var position = getPosition();
        positionDto.setId(null);

        when(positionRepository.saveAndFlush(any(Position.class)))
                .thenThrow(DataIntegrityViolationException.class);
        when(positionMapper.toEntity(positionDto))
                .thenReturn(position);

        assertThatThrownBy(() -> positionService.create(positionDto))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining(
                        "There is already position with name "
                        + POSITION_NAME + "."
                );

        verify(positionRepository, times(1))
                .saveAndFlush(any());
        verify(positionMapper, times(1))
                .toEntity(positionDto);
    }

    @Test
    void delete() {
        when(positionRepository.existsById(INDEPENDENT_POSITION_ID))
                .thenReturn(true);

        doNothing().when(entityManager)
                .flush();

        positionService.delete(INDEPENDENT_POSITION_ID);

        verify(positionRepository, times(1))
                .existsById(INDEPENDENT_POSITION_ID);
        verify(positionRepository, times(1))
                .deleteById(INDEPENDENT_POSITION_ID);
    }

    @Test
    void delete_notFound() {
        when(positionRepository.existsById(NON_EXISTENT_POSITION_ID))
                .thenReturn(false);

        assertThatThrownBy(() -> positionService.delete(NON_EXISTENT_POSITION_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Couldn't find position with id "
                        + NON_EXISTENT_POSITION_ID + "."
                );

        verify(positionRepository, times(1))
                .existsById(NON_EXISTENT_POSITION_ID);
        verify(positionRepository, never())
                .deleteById(NON_EXISTENT_POSITION_ID);
    }

    @Test
    void delete_withDependentEmployees() {
        when(positionRepository.existsById(DEPENDENT_POSITION_ID))
                .thenReturn(true);

        doThrow(ConstraintViolationException.class)
                .when(positionRepository).deleteById(DEPENDENT_POSITION_ID);

        assertThrows(
                ConstraintViolationException.class,
                () -> positionService.delete(DEPENDENT_POSITION_ID)
        );

        verify(positionRepository, times(1))
                .existsById(DEPENDENT_POSITION_ID);
        verify(positionRepository, times(1))
                .deleteById(DEPENDENT_POSITION_ID);
        verify(entityManager, never())
                .flush();
    }

    private Position getPosition() {
        return Position.builder()
                .id(POSITION_ID)
                .name(POSITION_NAME)
                .build();
    }

    private PositionDto getPositionDto() {
        return new PositionDto(
                POSITION_ID,
                POSITION_NAME
        );
    }
}
