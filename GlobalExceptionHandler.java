package com.project.Bootcamp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Handle UserAlreadyExistsException
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // Handle validation errors (MethodArgumentNotValidException)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return buildResponseEntity(errors.toString(), HttpStatus.BAD_REQUEST);
    }

    // Handle authentication errors (BadCredentialsException)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(org.springframework.security.authentication.BadCredentialsException ex) {
        return buildResponseEntity("Invalid username or password.", HttpStatus.UNAUTHORIZED);
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllOtherExceptions(Exception ex) {
        return buildResponseEntity("Internal Server Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Helper method to build response entities
    private ResponseEntity<Object> buildResponseEntity(String message, HttpStatus status) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", status.value());
        responseBody.put("error", status.getReasonPhrase());
        responseBody.put("message", message);

        return new ResponseEntity<>(responseBody, status);
    }
}
