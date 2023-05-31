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

@Slf4j
@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnClass(LoggingProperties.class)
@ConditionalOnProperty(prefix = "aop.logging", name = "enabled", havingValue = "true")
public class LoggingAutoConfiguration {

    @PostConstruct
    void init() {
        log.info("LoggingAutoConfiguration initialized");
    }

    @Bean
    @ConditionalOnMissingBean(LoggingAspect.class)
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }

}
