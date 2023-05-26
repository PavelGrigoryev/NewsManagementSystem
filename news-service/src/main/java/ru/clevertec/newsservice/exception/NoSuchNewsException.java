package ru.clevertec.newsservice.exception;

public class NoSuchNewsException extends NotFoundException {

    public NoSuchNewsException(String message) {
        super(message);
    }

}
