package ru.clevertec.loggingstarter.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.loggingstarter.aspect.LoggingAspect;

/**
 * The LoggingAutoConfiguration class is responsible for configuring logging aspects in a Spring Boot application.
 * This class enables the configuration properties for logging and initializes the LoggingAspect bean.
 * The class is enabled only if the "aop.logging.enabled" property is set to true in the application properties(yaml) file.
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnClass(LoggingProperties.class)
@ConditionalOnProperty(prefix = "aop.logging", name = "enabled", havingValue = "true")
public class LoggingAutoConfiguration {

    /**
     * Initializes the LoggingAutoConfiguration class and logs the initialization message.
     */
    @PostConstruct
    void init() {
        log.info("LoggingAutoConfiguration initialized");
    }

    /**
     * Returns a LoggingAspect bean if it is not already present in the Spring application context.
     *
     * @return The LoggingAspect bean.
     */
    @Bean
    @ConditionalOnMissingBean(LoggingAspect.class)
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }

}
