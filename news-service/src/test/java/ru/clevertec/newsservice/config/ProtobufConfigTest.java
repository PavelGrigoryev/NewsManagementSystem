package ru.clevertec.newsservice.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProtobufConfigTest {

    @Spy
    private ProtobufConfig protobufConfig;

    @Test
    @DisplayName("test should return not null ProtobufHttpMessageConverter")
    void testShouldReturnNotNullProtobufHttpMessageConverter() {
        ProtobufHttpMessageConverter converter = protobufConfig.protobufHttpMessageConverter();

        assertThat(converter).isNotNull();
    }

}
