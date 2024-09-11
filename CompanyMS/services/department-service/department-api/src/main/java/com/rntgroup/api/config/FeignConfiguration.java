package com.rntgroup.api.config;

import com.rntgroup.api.client.EmployeeClient;
import com.rntgroup.api.client.EmployeeClientFallbackFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(clients = EmployeeClient.class)
@ComponentScan(basePackageClasses = EmployeeClientFallbackFactory.class)
@Configuration
public class FeignConfiguration {
}
