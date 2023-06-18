package ru.clevertec.exceptionhandlerstarter.exception;

public class AccessDeniedForThisRoleException extends RuntimeException {
    
    public AccessDeniedForThisRoleException(String message) {
        super(message);
    }
    
}
