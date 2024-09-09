package com.rntroup.api.config;

import com.rntroup.api.client.DepartmentClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(clients = DepartmentClient.class)
@Configuration
public class FeignConfiguration {
}
