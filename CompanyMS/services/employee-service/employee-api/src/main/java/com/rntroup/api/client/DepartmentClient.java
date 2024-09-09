package com.rntroup.api.client;

import com.rntroup.api.dto.DepartmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "department-service",
        url = "${application.config.department-url}"
)
public interface DepartmentClient {

    @GetMapping("/{id}")
    DepartmentDto getById(@PathVariable final Integer id);
}