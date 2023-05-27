package ru.clevertec.newsservice.exception.model;

public record Violation(String fieldName,
                        String message) {
}
