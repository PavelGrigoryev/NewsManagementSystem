package ru.clevertec.loggingstarter.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Slf4j
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "aop.logging")
public class LoggingProperties {

    /**
     * to enable common logging aop on service layer
     */
    private boolean enabled;

    @PostConstruct
    void init() {
        log.info("LoggingProperties initialized: {}", this);
    }

}
