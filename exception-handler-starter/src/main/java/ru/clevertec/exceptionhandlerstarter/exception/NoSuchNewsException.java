package ru.clevertec.exceptionhandlerstarter.exception;

public class NoSuchNewsException extends NotFoundException {

    public NoSuchNewsException(String message) {
        super(message);
    }

}
