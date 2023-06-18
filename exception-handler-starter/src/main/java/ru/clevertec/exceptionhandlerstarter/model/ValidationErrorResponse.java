package ru.clevertec.exceptionhandlerstarter.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ValidationErrorResponse(String errorCode,
                                      List<Violation> violations) {
}
