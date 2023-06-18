package ru.clevertec.exceptionhandlerstarter.exception;

public class NoSuchCommentException extends NotFoundException {

    public NoSuchCommentException(String message) {
        super(message);
    }

}
