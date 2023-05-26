package ru.clevertec.newsservice.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.newsservice.exception.NotFoundException;
import ru.clevertec.newsservice.exception.model.IncorrectData;

@ControllerAdvice
public class NewsServiceExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<IncorrectData> notFoundException(NotFoundException exception) {
        return getResponse(exception.getClass().getSimpleName(), exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    private static ResponseEntity<IncorrectData> getResponse(String name, String message, HttpStatus status) {
        IncorrectData incorrectData = new IncorrectData(name, message, status.toString());
        return ResponseEntity.status(status).body(incorrectData);
    }

}
