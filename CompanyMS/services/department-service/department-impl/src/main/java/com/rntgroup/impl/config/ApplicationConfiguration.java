package com.rntgroup.impl.config;

import com.rntgroup.api.config.AdviceConfiguration;
import com.rntgroup.api.config.FeignConfiguration;
import com.rntgroup.api.config.OpenApiConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Import({FeignConfiguration.class,
        AdviceConfiguration.class,
        OpenApiConfiguration.class})
@EnableScheduling
@Configuration
public class ApplicationConfiguration {
}
