package com.azry.library_management_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserRoleNotFoundException extends RuntimeException {
    public UserRoleNotFoundException(final String message) {
        super(message);
    }
}
