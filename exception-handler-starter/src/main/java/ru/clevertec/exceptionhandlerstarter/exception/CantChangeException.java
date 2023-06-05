package ru.clevertec.exceptionhandlerstarter.exception;

public class CantChangeException extends RuntimeException {

    public CantChangeException(String message) {
        super(message);
    }

}
