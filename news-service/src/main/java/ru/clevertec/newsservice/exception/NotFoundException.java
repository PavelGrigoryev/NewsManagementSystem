package ru.clevertec.newsservice.exception;

public abstract class NotFoundException extends RuntimeException {

    protected NotFoundException(String message) {
        super(message);
    }

}
