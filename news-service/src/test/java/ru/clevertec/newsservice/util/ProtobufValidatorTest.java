package ru.clevertec.newsservice.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.clevertec.exceptionhandlerstarter.exception.ProtoValidationException;
import ru.clevertec.newsservice.dto.proto.NewsRequest;
import ru.clevertec.newsservice.util.testbuilder.news.NewsRequestTestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProtobufValidatorTest {

    @Test
    @DisplayName("test should not throw ProtoValidationException")
    void testShouldNotThrowProtoValidationException() {
        NewsRequest request = NewsRequestTestBuilder.aNewsRequest().build();

        assertDoesNotThrow(() -> ProtobufValidator.validateProto(request));
    }

    @Test
    @DisplayName("test should throw ProtoValidationException")
    void testShouldThrowProtoValidationException() {
        NewsRequest request = NewsRequestTestBuilder.aNewsRequest().withTitle("").build();

        assertThrows(ProtoValidationException.class, () -> ProtobufValidator.validateProto(request));
    }

    @Test
    @DisplayName("test should throw ProtoValidationException with expected message")
    void testShouldThrowProtoValidationExceptionWithExpectedMessage() {
        NewsRequest request = NewsRequestTestBuilder.aNewsRequest().withTitle("A").build();
        String expectedMessage = ".NewsRequest.title: length must be at least 3 but got: 1 - Got \"A\"";

        Exception exception = assertThrows(ProtoValidationException.class,
                () -> ProtobufValidator.validateProto(request));
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

}
