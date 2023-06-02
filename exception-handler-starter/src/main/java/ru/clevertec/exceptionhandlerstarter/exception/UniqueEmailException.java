package ru.clevertec.exceptionhandlerstarter.exception;

public class UniqueEmailException extends RuntimeException {

    public UniqueEmailException(String message) {
        super(message);
    }

}
