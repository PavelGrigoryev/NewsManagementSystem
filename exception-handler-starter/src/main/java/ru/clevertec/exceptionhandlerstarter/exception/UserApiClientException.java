package ru.clevertec.exceptionhandlerstarter.exception;

import lombok.Getter;

@Getter
public class UserApiClientException extends RuntimeException {

    private final String exceptionName;
    private final Integer errorCode;

    public UserApiClientException(String message, String exceptionName, Integer errorCode) {
        super(message);
        this.exceptionName = exceptionName;
        this.errorCode = errorCode;
    }

}
