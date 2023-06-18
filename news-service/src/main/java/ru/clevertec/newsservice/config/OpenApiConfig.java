package ru.clevertec.newsservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class provides configuration for the OpenAPI documentation of the News Service application.
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "News Service Application"),
        servers = @Server(url = "http://localhost:8080"))
@SecurityScheme(name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer")
public class OpenApiConfig {

    /**
     * Creates a Spring Bean for configuring a ModelResolver with an ObjectMapper that has a registered ProtobufModule.
     * Without it, swagger-ui will fail.
     *
     * @return ModelResolver with ObjectMapper configured with ProtobufModule.
     */
    @Bean
    public ModelResolver modelResolver() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ProtobufModule());
        return new ModelResolver(objectMapper);
    }

}
