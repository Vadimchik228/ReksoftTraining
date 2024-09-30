package com.rntroup.api.aop;

import io.prometheus.client.Histogram;
import io.prometheus.client.exporter.PushGateway;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EmployeeCreationTimeAspect {
    private static final String JOB_NAME = "pushgateway";
    private static final PushGateway PUSH_GATEWAY = new PushGateway("localhost:9091");
    private static final Histogram EMPLOYEE_CREATION_TIME = Histogram.build()
            .name("employee_creation_time_seconds")
            .help("Time taken to create an employee")
            .register();

    @Pointcut("execution(* com.rntroup.api.controller.EmployeeController.create(..))")
    public void createEmployeeMethod() {
    }

    @Around("createEmployeeMethod()")
    public Object measureEmployeeCreationTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            EMPLOYEE_CREATION_TIME.observe(executionTime);
            PUSH_GATEWAY.pushAdd(EMPLOYEE_CREATION_TIME, JOB_NAME);
        }
    }
}
