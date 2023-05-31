package ru.clevertec.exceptionhandlerstarter.model;

public record IncorrectData(String exception,
                            String errorMessage,
                            String errorCode) {
}
