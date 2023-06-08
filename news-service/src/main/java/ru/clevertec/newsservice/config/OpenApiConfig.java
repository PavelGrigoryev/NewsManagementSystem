package ru.clevertec.newsservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * This class provides configuration for the OpenAPI documentation of the News Service application.
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "News Service Application"),
        servers = @Server(url = "http://localhost:8080"))
public class OpenApiConfig {
}
