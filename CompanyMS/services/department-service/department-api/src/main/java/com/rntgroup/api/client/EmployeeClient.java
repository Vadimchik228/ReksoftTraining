package com.rntgroup.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(
        name = "employee-service",
        url = "${application.config.employee-url}"
)
public interface EmployeeClient {

    @GetMapping("/{departmentId}/count")
    Long getCountOfAllByDepartmentId(@PathVariable final Integer departmentId);

    @GetMapping("/{departmentId}/salaryFund")
    BigDecimal getSalaryFundByDepartmentId(@PathVariable final Integer departmentId);
}