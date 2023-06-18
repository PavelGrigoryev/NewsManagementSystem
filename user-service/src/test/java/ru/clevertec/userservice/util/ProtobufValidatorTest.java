package ru.clevertec.userservice.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.clevertec.exceptionhandlerstarter.exception.ProtoValidationException;
import ru.clevertec.userservice.dto.proto.UserRegisterRequest;
import ru.clevertec.userservice.util.testbuilder.UserRegisterRequestTestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProtobufValidatorTest {

    @Test
    @DisplayName("test should not throw ProtoValidationException")
    void testShouldNotThrowProtoValidationException() {
        UserRegisterRequest request = UserRegisterRequestTestBuilder.aUserRegisterRequest().build();

        assertDoesNotThrow(() -> ProtobufValidator.validateProto(request));
    }

    @Test
    @DisplayName("test should throw ProtoValidationException")
    void testShouldThrowProtoValidationException() {
        UserRegisterRequest request = UserRegisterRequestTestBuilder.aUserRegisterRequest().withEmail("email").build();

        assertThrows(ProtoValidationException.class, () -> ProtobufValidator.validateProto(request));
    }

    @Test
    @DisplayName("test should throw ProtoValidationException with expected message")
    void testShouldThrowProtoValidationExceptionWithExpectedMessage() {
        UserRegisterRequest request = UserRegisterRequestTestBuilder.aUserRegisterRequest().withEmail("email").build();
        String expectedMessage = ".UserRegisterRequest.email: should be a valid email - Got \"email\"";

        Exception exception = assertThrows(ProtoValidationException.class,
                () -> ProtobufValidator.validateProto(request));
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

}
