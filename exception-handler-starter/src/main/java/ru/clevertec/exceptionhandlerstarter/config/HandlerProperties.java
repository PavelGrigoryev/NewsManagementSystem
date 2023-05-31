package ru.clevertec.exceptionhandlerstarter.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The HandlerProperties class that can be used to enable exception handling on the service layer.
 */
@Slf4j
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "exception.handling")
public class HandlerProperties {

    /**
     * to enable common exception handling
     */
    private boolean enabled;

    /**
     * This method logs a message that indicates the successful initialization of the HandlerProperties bean along with
     * its current state.
     */
    @PostConstruct
    void init() {
        log.info("HandlerProperties initialized: {}", this);
    }

}
