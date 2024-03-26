package com.project.proo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.FieldError;
import jakarta.validation.ConstraintViolation;

import org.springframework.http.HttpStatus;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationException(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());

        StringBuilder errorMessageBuilder = new StringBuilder();

        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
                errorMessageBuilder.append(fieldError.getDefaultMessage()).append("; ");
            }
        } else if (e instanceof ConstraintViolationException) {
            ConstraintViolationException ex = (ConstraintViolationException) e;
            for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
                errorMessageBuilder.append(violation.getMessage()).append("; ");
            }
        } else {
            errorMessageBuilder.append("Validation failed");
        }

        String errorMessage = errorMessageBuilder.toString().replaceAll("; $", ""); // Remove trailing semicolon and space
        response.put("error", errorMessage);

        return ResponseEntity.badRequest().body(response);
    }
}