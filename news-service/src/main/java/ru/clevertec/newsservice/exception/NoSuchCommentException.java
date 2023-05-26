package ru.clevertec.newsservice.exception;

public class NoSuchCommentException extends NotFoundException {

    public NoSuchCommentException(String message) {
        super(message);
    }

}
