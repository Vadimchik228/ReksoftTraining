package com.rntgroup.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(
        name = "employee-service",
        url = "${application.config.employee-url}",
        fallbackFactory = EmployeeClientFallbackFactory.class
)
public interface EmployeeClient {

    @GetMapping("/{departmentId}/count")
    Long getCountOfAllByDepartmentId(@PathVariable Integer departmentId);

    @GetMapping("/{departmentId}/salaryFund")
    BigDecimal getSalaryFundByDepartmentId(@PathVariable Integer departmentId);

}