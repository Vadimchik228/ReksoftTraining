package com.rntgroup.api.config;

import com.rntgroup.api.client.EmployeeClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(clients = EmployeeClient.class)
@Configuration
public class FeignConfiguration {
}
