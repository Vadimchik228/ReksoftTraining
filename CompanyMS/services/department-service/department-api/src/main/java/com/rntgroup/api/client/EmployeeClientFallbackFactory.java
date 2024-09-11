package com.rntgroup.api.client;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EmployeeClientFallbackFactory implements FallbackFactory<EmployeeClient> {

    @Override
    public EmployeeClient create(Throwable cause) {
        return new EmployeeClientFallback();
    }

    private static class EmployeeClientFallback implements EmployeeClient {
        private static final BigDecimal DEFAULT_SALARY_FUND = new BigDecimal("100000.00");

        @Override
        public Long getCountOfAllByDepartmentId(Integer departmentId) {
            return null;
        }

        @Override
        public BigDecimal getSalaryFundByDepartmentId(Integer departmentId) {
            return DEFAULT_SALARY_FUND;
        }
    }
}
