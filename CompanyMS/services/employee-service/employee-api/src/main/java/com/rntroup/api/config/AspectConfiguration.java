package com.rntroup.api.config;

import com.rntroup.api.aop.EmployeeCreationTimeAspect;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackageClasses = EmployeeCreationTimeAspect.class)
@Configuration
public class AspectConfiguration {
}
