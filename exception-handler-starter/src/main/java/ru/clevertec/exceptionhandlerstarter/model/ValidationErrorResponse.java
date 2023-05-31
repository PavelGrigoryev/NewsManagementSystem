package ru.clevertec.exceptionhandlerstarter.model;

import java.util.List;

public record ValidationErrorResponse(String errorMessage,
                                      List<Violation> violations) {
}
