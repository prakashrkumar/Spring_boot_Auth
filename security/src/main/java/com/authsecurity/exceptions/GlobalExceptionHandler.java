package com.authsecurity.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.authsecurity.dtos.ApiError;
import com.authsecurity.dtos.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
        logger.info("Exception: {}", exception.getClass().getName());
        ErrorResponse error = new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND, 404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        ErrorResponse error = new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, 400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler({
        UsernameNotFoundException.class,
        BadCredentialsException.class,
        CredentialsExpiredException.class,
        DisabledException.class
    })
    public ResponseEntity<ApiError> handleAuthException(Exception e, HttpServletRequest request) {
        var apiError = ApiError.of(HttpStatus.BAD_REQUEST.value(), "Bad Request", e.getMessage(), request.getRequestURI());
        return ResponseEntity.badRequest().body(apiError);
    }
}
