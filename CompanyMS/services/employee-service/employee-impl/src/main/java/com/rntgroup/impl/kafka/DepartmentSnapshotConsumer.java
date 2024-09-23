package com.rntgroup.impl.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.impl.mapper.SimpleDepartmentMapper;
import com.rntgroup.impl.repository.DepartmentSnapshotRepository;
import com.rntroup.api.dto.SimpleDepartmentDto;
import com.rntroup.api.exception.DtoDeserializationException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentSnapshotConsumer {

    private final DepartmentSnapshotRepository departmentRepository;
    private final SimpleDepartmentMapper departmentMapper;
    private final ObjectMapper objectMapper;
    private final EntityManager entityManager;

    @KafkaListener(topics = "department_events", groupId = "employee-group")
    public void consumeDepartmentEvents(ConsumerRecord<String, String> record) {
        var key = record.key();
        var value = record.value();

        if ("deleted".equals(value)) {
            departmentRepository.deleteById(Integer.parseInt(key));
            entityManager.flush();
        } else {
            try {
                var dto = objectMapper.readValue(value, SimpleDepartmentDto.class);
                departmentRepository.saveAndFlush(departmentMapper.toEntity(dto));
            } catch (JsonProcessingException e) {
                throw new DtoDeserializationException(
                        "Error while deserializing JSON to simple department dto."
                );
            }
        }
    }

}
