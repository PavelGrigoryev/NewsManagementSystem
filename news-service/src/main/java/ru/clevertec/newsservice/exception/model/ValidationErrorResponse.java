package ru.clevertec.newsservice.exception.model;

import java.util.List;

public record ValidationErrorResponse(String errorMessage,
                                      List<Violation> violations) {
}
