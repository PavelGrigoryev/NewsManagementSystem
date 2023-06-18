package ru.clevertec.newsservice.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.clevertec.exceptionhandlerstarter.exception.UserApiClientException;

import java.io.IOException;
import java.io.InputStream;

/**
 * The UserApiErrorDecoder class implements ErrorDecoder interface from Feign that handles error responses from the User API.
 * It reads the error response body and extracts the exception name and error message to create a custom exception.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserApiErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    /**
     * Decodes the error response and creates a custom exception with the extracted exception name, error message,
     * and status code.
     *
     * @param methodKey The method key of the request that triggered the error response.
     * @param response  The error response received from the User API.
     * @return A custom exception with the extracted exception name, error message, and status code.
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        String exceptionName = "UserApiClientException";
        String errorMessage = "Bad credentials";
        if (response.body() != null) {
            try (InputStream responseBody = response.body().asInputStream()) {
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                exceptionName = jsonNode.at("/exception").asText();
                errorMessage = jsonNode.at("/error_message").asText();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return new UserApiClientException(errorMessage, exceptionName, response.status());
    }

}
