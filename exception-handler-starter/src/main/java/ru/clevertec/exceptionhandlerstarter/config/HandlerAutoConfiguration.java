package ru.clevertec.exceptionhandlerstarter.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.exceptionhandlerstarter.handler.NewsServiceExceptionHandler;
import ru.clevertec.loggingstarter.aspect.LoggingAspect;

/**
 * The HandlerAutoConfiguration class is responsible for configuring NewsServiceExceptionHandler in a Spring Boot application.
 * This class enables the configuration properties for exception handling and initializes the NewsServiceExceptionHandler bean.
 * The class is enabled only if the "exception.handling.enabled" property is set to true in the application properties(yaml) file.
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(HandlerProperties.class)
@ConditionalOnClass(HandlerProperties.class)
@ConditionalOnProperty(prefix = "exception.handling", name = "enabled", havingValue = "true")
public class HandlerAutoConfiguration {

    /**
     * Initializes the HandlerAutoConfiguration class and logs the initialization message.
     */
    @PostConstruct
    void init() {
        log.info("HandlerAutoConfiguration initialized");
    }

    /**
     * Returns a NewsServiceExceptionHandler bean if it is not already present in the Spring application context.
     *
     * @return The NewsServiceExceptionHandler bean.
     */
    @Bean
    @ConditionalOnMissingBean(LoggingAspect.class)
    public NewsServiceExceptionHandler newsServiceExceptionHandler() {
        return new NewsServiceExceptionHandler();
    }

}
