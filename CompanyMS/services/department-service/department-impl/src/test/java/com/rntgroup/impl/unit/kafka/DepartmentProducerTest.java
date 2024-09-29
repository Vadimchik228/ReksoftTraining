package com.rntgroup.impl.unit.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.api.dto.SimpleDepartmentDto;
import com.rntgroup.api.exception.DtoSerializationException;
import com.rntgroup.impl.entity.Department;
import com.rntgroup.impl.kafka.DepartmentProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DepartmentProducerTest {
    private static final String TOPIC = "department_events";
    private DepartmentProducer departmentProducer;
    private KafkaTemplate<String, String> kafkaTemplateMock;
    private ObjectMapper objectMapperMock;

    @BeforeEach
    void setUp() {
        objectMapperMock = mock(ObjectMapper.class);
        kafkaTemplateMock = mock(KafkaTemplate.class);

        departmentProducer = new DepartmentProducer(
                objectMapperMock,
                kafkaTemplateMock
        );
    }

    @Test
    void sendDepartmentCreated() throws JsonProcessingException {
        var department = Department.builder()
                .id(1)
                .name("Test Department")
                .creationDate(LocalDate.now())
                .parentDepartment(null)
                .build();

        var departmentDto = new SimpleDepartmentDto(
                department.getId(),
                department.getName()
        );

        var json = "{\"id\":1,\"name\":\"Test Department\"}";

        when(objectMapperMock.writeValueAsString(departmentDto))
                .thenReturn(json);

        departmentProducer.sendDepartmentCreated(department);

        verify(kafkaTemplateMock).send(
                eq(TOPIC),
                eq("1"),
                eq(json)
        );
    }

    @Test
    void sendDepartmentCreated_shouldThrowDtoSerializationException()
            throws JsonProcessingException {

        var department = Department.builder()
                .id(1)
                .name("Test Department")
                .creationDate(LocalDate.now())
                .build();

        var departmentDto = new SimpleDepartmentDto(
                department.getId(),
                department.getName()
        );

        when(objectMapperMock.writeValueAsString(departmentDto))
                .thenThrow(JsonProcessingException.class);

        var exception = assertThrows(
                DtoSerializationException.class,
                () -> departmentProducer.sendDepartmentCreated(department)
        );

        assertEquals(
                "Error while serializing simple department dto to JSON.",
                exception.getMessage()
        );
    }

    @Test
    void sendDepartmentDeleted_shouldSendDepartmentIdAndDeletedMessageToKafka() {
        var departmentId = 1;

        departmentProducer.sendDepartmentDeleted(departmentId);

        verify(kafkaTemplateMock).send(
                eq(TOPIC),
                eq("1"),
                eq("deleted")
        );
    }
}
