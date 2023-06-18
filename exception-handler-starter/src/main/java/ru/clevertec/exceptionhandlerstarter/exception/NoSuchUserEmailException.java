package ru.clevertec.exceptionhandlerstarter.exception;

public class NoSuchUserEmailException extends NotFoundException {

    public NoSuchUserEmailException(String message) {
        super(message);
    }

}
