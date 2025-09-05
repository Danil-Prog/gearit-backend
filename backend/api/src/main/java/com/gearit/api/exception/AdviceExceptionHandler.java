package com.gearit.api.exception;

import com.gearit.api.exception.violation.ValidationErrorResponse;
import com.gearit.api.exception.violation.Violation;
import com.gearit.common.exception.WebClientException;
import com.gearit.common.http.response.WebClientExceptionResponse;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> new Violation(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .collect(Collectors.toList());

        return new ValidationErrorResponse(violations);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestResponseException> badRequestException(BadRequestException ex) {
        BadRequestResponseException badRequestResponseException = new BadRequestResponseException(ex.getMessage());
        return new ResponseEntity<>(badRequestResponseException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WebClientException.class)
    public ResponseEntity<WebClientExceptionResponse> webClientException(WebClientException ex) {
        WebClientExceptionResponse webClientExceptionResponse = new WebClientExceptionResponse(ex.getMessage(), ex.getExtendedHelp());
        return new ResponseEntity<>(webClientExceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
