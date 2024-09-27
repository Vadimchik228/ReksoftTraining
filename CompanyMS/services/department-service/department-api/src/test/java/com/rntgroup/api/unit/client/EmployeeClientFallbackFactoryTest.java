package com.rntgroup.api.unit.client;

import com.rntgroup.api.client.EmployeeClientFallbackFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class EmployeeClientFallbackFactoryTest {

    private final EmployeeClientFallbackFactory employeeClientFallbackFactory =
            new EmployeeClientFallbackFactory();

    @Test
    public void getCountOfAllByDepartmentId_fallback() {
        var employeeClient = employeeClientFallbackFactory
                .create(new Throwable("Connection refused"));

        assertThat(employeeClient.getCountOfAllByDepartmentId(1))
                .isNull();
    }

    @Test
    public void getSalaryFundByDepartmentId_fallback() {
        var employeeClient = employeeClientFallbackFactory
                .create(new Throwable("Connection refused"));

        assertThat(employeeClient.getSalaryFundByDepartmentId(1))
                .isEqualTo(new BigDecimal("100000.00"));
    }
}
