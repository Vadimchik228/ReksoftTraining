package com.rntroup.api.config;

import com.rntroup.api.controller.ControllerAdvice;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackageClasses = ControllerAdvice.class)
@Configuration
public class ControllerAdviceConfiguration {
}
