package ru.clevertec.exceptionhandlerstarter.exception;

public class UserDoesNotHavePermissionException extends RuntimeException {

    public UserDoesNotHavePermissionException(String message) {
        super(message);
    }

}
