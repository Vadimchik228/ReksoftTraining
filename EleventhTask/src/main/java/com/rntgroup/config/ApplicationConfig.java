package com.rntgroup.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DatabaseConfig.class)
@ComponentScan(basePackages = {"com.rntgroup.repository"})
public class ApplicationConfig {
}
