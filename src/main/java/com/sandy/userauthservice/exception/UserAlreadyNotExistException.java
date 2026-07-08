package com.sandy.userauthservice.exception;

public class UserAlreadyNotExistException extends RuntimeException {
    public UserAlreadyNotExistException(String message) {
        super(message);
    }
}
