package ru.clevertec.userservice.config;

import io.swagger.v3.core.jackson.ModelResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class OpenApiConfigTest {

    @Spy
    private OpenApiConfig openApiConfig;

    @Test
    @DisplayName("test should return not null ModelResolver")
    void testShouldReturnNotNullModelResolver() {
        ModelResolver modelResolver = openApiConfig.modelResolver();

        assertThat(modelResolver).isNotNull();
    }

}
