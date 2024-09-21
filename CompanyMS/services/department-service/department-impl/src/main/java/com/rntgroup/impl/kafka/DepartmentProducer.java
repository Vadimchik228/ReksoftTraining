package com.rntgroup.impl.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.api.dto.SimpleDepartmentDto;
import com.rntgroup.api.exception.DtoSerializationException;
import com.rntgroup.impl.entity.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentProducer {
    private static final String TOPIC_NAME = "department_events";

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendDepartmentCreated(final Department department) {
        SimpleDepartmentDto departmentDto = new SimpleDepartmentDto(
                department.getId(),
                department.getName()
        );

        try {
            kafkaTemplate.send(TOPIC_NAME,
                    String.valueOf(departmentDto.id()),
                    objectMapper.writeValueAsString(departmentDto)
            );
        } catch (JsonProcessingException e) {
            throw new DtoSerializationException(
                    "Error while serializing simple department dto to JSON."
            );
        }
    }

    public void sendDepartmentDeleted(final Integer departmentId) {
        kafkaTemplate.send(TOPIC_NAME, departmentId.toString(), "deleted");
    }

}
