package com.sandy.userauthservice.exception;

public class UserNotRegisteredException extends RuntimeException{
    public UserNotRegisteredException(String message) {
        super(message);
    }
}
