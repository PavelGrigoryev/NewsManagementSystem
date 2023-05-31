package ru.clevertec.exceptionhandlerstarter.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Slf4j
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "exception.handling")
public class HandlerProperties {

    /**
     * to enable common exception handling
     */
    private boolean enabled;

    @PostConstruct
    void init() {
        log.info("HandlerProperties initialized: {}", this);
    }

}
