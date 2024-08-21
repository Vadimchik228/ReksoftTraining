package com.rntgroup.task1;

import com.rntgroup.task1.config.ApplicationConfig;
import com.rntgroup.task1.services.EmulationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class FirstApplicationRunner {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        EmulationService emulator = context.getBean("emulationService", EmulationService.class);
        emulator.emulateCompetition();
    }
}
