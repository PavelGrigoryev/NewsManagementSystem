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

@Slf4j
@Configuration
@EnableConfigurationProperties(HandlerProperties.class)
@ConditionalOnClass(HandlerProperties.class)
@ConditionalOnProperty(prefix = "exception.handling", name = "enabled", havingValue = "true")
public class HandlerAutoConfiguration {

    @PostConstruct
    void init() {
        log.info("HandlerAutoConfiguration initialized");
    }

    @Bean
    @ConditionalOnMissingBean(LoggingAspect.class)
    public NewsServiceExceptionHandler newsServiceExceptionHandler() {
        return new NewsServiceExceptionHandler();
    }

}
