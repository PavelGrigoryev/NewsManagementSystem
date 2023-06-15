package ru.clevertec.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;

/**
 * A configuration class for using Protobuf with Spring Rest.
 */
@Configuration
public class ProtobufConfig {

    /**
     * Provides a bean for the {@link ProtobufHttpMessageConverter}.
     *
     * @return the ProtobufHttpMessageConverter bean.
     */
    @Bean
    public ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }

}
