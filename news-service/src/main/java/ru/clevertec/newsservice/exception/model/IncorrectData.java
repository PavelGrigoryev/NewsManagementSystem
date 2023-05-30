package ru.clevertec.newsservice.exception.model;

public record IncorrectData(String exception,
                            String errorMessage,
                            String errorCode) {
}
