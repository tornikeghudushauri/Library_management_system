package com.azry.library_management_system.exception.handler;

import com.azry.library_management_system.exception.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            BookNotFoundException.class,
            UserNotFoundException.class,
            UserRoleNotFoundException.class
    })
    protected ResponseEntity<Object> handleNotFoundExceptions(final RuntimeException ex) {
        String bodyOfResponse = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bodyOfResponse);
    }

    @ExceptionHandler(value = {
            BookAlreadyBorrowedException.class,
            BookAlreadyReturnedException.class
    })
    protected ResponseEntity<Object> handleConflictExceptions(final RuntimeException ex) {
        String bodyOfResponse = ex.getMessage();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(bodyOfResponse);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    protected ResponseEntity<Object> handleAuthenticationFailedException(final AuthenticationFailedException ex) {
        String bodyOfResponse = ex.getMessage();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(bodyOfResponse);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(final Exception ex) {
        String bodyOfResponse = ex.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(bodyOfResponse);
    }

}
