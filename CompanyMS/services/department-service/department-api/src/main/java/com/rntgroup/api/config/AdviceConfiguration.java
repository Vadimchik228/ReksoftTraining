package com.rntgroup.api.config;

import com.rntgroup.api.controller.ControllerAdvice;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackageClasses = ControllerAdvice.class)
@Configuration
public class AdviceConfiguration {
}
