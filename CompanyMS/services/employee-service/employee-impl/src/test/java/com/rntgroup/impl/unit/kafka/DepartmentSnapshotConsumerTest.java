package com.rntgroup.impl.unit.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.impl.entity.DepartmentSnapshot;
import com.rntgroup.impl.kafka.DepartmentSnapshotConsumer;
import com.rntgroup.impl.mapper.SimpleDepartmentMapper;
import com.rntgroup.impl.repository.DepartmentSnapshotRepository;
import com.rntroup.api.dto.SimpleDepartmentDto;
import com.rntroup.api.exception.DtoDeserializationException;
import jakarta.persistence.EntityManager;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DepartmentSnapshotConsumerTest {
    private static final String TOPIC = "department_events";

    private DepartmentSnapshotConsumer departmentSnapshotConsumer;
    private DepartmentSnapshotRepository departmentRepositoryMock;
    private SimpleDepartmentMapper departmentMapperMock;
    private ObjectMapper objectMapperMock;
    private EntityManager entityManagerMock;

    @BeforeEach
    void setUp() {
        departmentRepositoryMock = mock(DepartmentSnapshotRepository.class);
        departmentMapperMock = mock(SimpleDepartmentMapper.class);
        objectMapperMock = mock(ObjectMapper.class);
        entityManagerMock = mock(EntityManager.class);
        departmentSnapshotConsumer = new DepartmentSnapshotConsumer(
                departmentRepositoryMock,
                departmentMapperMock,
                objectMapperMock,
                entityManagerMock
        );
    }

    @Test
    void consumeDepartmentEvents_shouldDeleteDepartment() {
        String key = "1";
        String value = "deleted";
        var record = new ConsumerRecord<>(
                TOPIC,
                0,
                0,
                key,
                value
        );

        departmentSnapshotConsumer.consumeDepartmentEvents(record);

        verify(departmentRepositoryMock).deleteById(Integer.parseInt(key));
        verify(entityManagerMock).flush();
    }

    @Test
    void consumeDepartmentEvents_shouldSaveDepartment() throws JsonProcessingException {
        String key = "1";
        String value = "{\"id\":1,\"name\":\"Test Department\"}";
        var departmentDto = new SimpleDepartmentDto(
                1,
                "Test Department"
        );
        var department = new DepartmentSnapshot(
                1,
                "Test Department"
        );
        var record = new ConsumerRecord<>(
                TOPIC,
                0,
                0,
                key,
                value
        );

        when(objectMapperMock.readValue(value, SimpleDepartmentDto.class))
                .thenReturn(departmentDto);
        when(departmentMapperMock.toEntity(departmentDto))
                .thenReturn(department);

        departmentSnapshotConsumer.consumeDepartmentEvents(record);

        verify(objectMapperMock)
                .readValue(value, SimpleDepartmentDto.class);
        verify(departmentMapperMock)
                .toEntity(departmentDto);
        verify(departmentRepositoryMock)
                .saveAndFlush(department);
    }

    @Test
    void consumeDepartmentEvents_shouldThrowDtoDeserializationException()
            throws JsonProcessingException {
        String key = "1";
        String value = "{\"id\":1,\"name\":\"Test Department\"}";
        var record = new ConsumerRecord<>(
                TOPIC,
                0,
                0,
                key,
                value
        );

        when(objectMapperMock.readValue(value, SimpleDepartmentDto.class))
                .thenThrow(JsonProcessingException.class);

        assertThatThrownBy(() -> departmentSnapshotConsumer
                .consumeDepartmentEvents(record))
                .isInstanceOf(DtoDeserializationException.class)
                .hasMessageContaining(
                        "Error while deserializing JSON to simple department dto."
                );

        verify(objectMapperMock)
                .readValue(value, SimpleDepartmentDto.class);
    }
}
