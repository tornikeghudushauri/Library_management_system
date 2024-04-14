package com.azry.library_management_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BookAlreadyBorrowedException extends RuntimeException {
    public BookAlreadyBorrowedException(final String message) {
        super(message);
    }
}
