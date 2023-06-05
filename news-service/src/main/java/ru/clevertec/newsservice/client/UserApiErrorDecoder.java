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

@Slf4j
@Component
@RequiredArgsConstructor
public class UserApiErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        String exceptionName = "UserApiClientException";
        String errorMessage = "Bad credentials";
        if (response.body() != null) {
            try (InputStream responseBody = response.body().asInputStream()) {
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                exceptionName = jsonNode.at("/exception").asText();
                errorMessage = jsonNode.at("/errorMessage").asText();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return new UserApiClientException(errorMessage, exceptionName, response.status());
    }

}
