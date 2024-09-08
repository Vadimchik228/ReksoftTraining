package com.rntgroup.impl.config;

import com.rntroup.api.config.ControllerAdviceConfiguration;
import com.rntroup.api.config.FeignConfiguration;
import com.rntroup.api.config.OpenApiConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({FeignConfiguration.class,
        ControllerAdviceConfiguration.class,
        OpenApiConfiguration.class})
@Configuration
public class ApplicationConfiguration {
}
