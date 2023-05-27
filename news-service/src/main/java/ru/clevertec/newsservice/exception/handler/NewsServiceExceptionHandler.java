package ru.clevertec.newsservice.exception.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.newsservice.exception.NotFoundException;
import ru.clevertec.newsservice.exception.model.IncorrectData;
import ru.clevertec.newsservice.exception.model.ValidationErrorResponse;
import ru.clevertec.newsservice.exception.model.Violation;

import java.util.List;

/**
 * This NewsServiceExceptionHandler class handles exceptions and returns appropriate error responses.
 */
@ControllerAdvice
public class NewsServiceExceptionHandler {

    /**
     * Handles {@link NotFoundException} and returns a 404 Not Found response with an error message.
     *
     * @param exception The NotFoundException to handle.
     * @return A ResponseEntity containing an {@link IncorrectData} object and a 404 status code.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<IncorrectData> notFoundException(NotFoundException exception) {
        return getResponse(exception.getClass().getSimpleName(), exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles {@link PropertyReferenceException} and returns a 406 Not Acceptable response with an error message.
     *
     * @param exception The PropertyReferenceException to handle.
     * @return A ResponseEntity containing an {@link IncorrectData} object and a 406 status code.
     */
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<IncorrectData> propertyReferenceException(PropertyReferenceException exception) {
        return getResponse(exception.getClass().getSimpleName(), exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Handles {@link ConstraintViolationException} and returns a 409 Conflict response with an error message.
     *
     * @param exception The ConstraintViolationException to handle.
     * @return A ResponseEntity containing an {@link ValidationErrorResponse} object and a 409 status code.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> constraintValidationException(ConstraintViolationException exception) {
        List<Violation> violations = exception.getConstraintViolations()
                .stream()
                .map(constraintViolation -> new Violation(constraintViolation.getPropertyPath().toString(),
                        constraintViolation.getMessage()))
                .toList();
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ValidationErrorResponse(HttpStatus.CONFLICT.toString(), violations));
    }

    /**
     * Handles {@link MethodArgumentNotValidException} and returns a 409 Conflict response with an error message.
     *
     * @param exception The MethodArgumentNotValidException to handle.
     * @return A ResponseEntity containing an {@link ValidationErrorResponse} object and a 409 status code.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<Violation> violations = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new Violation(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ValidationErrorResponse(HttpStatus.CONFLICT.toString(), violations));
    }

    /**
     * Creates response {@link ResponseEntity} with {@link IncorrectData}.
     *
     * @param name    the simple name of exception
     * @param message the message from exception
     * @param status  the {@link HttpStatus} code
     * @return A ResponseEntity containing an IncorrectData object and a status code.
     */
    private static ResponseEntity<IncorrectData> getResponse(String name, String message, HttpStatus status) {
        IncorrectData incorrectData = new IncorrectData(name, message, status.toString());
        return ResponseEntity.status(status).body(incorrectData);
    }

}
