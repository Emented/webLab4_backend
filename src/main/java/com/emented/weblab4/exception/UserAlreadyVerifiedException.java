package com.emented.weblab4.exception;

public class UserAlreadyVerifiedException extends RuntimeException {

    public UserAlreadyVerifiedException(String message) {
        super(message);
    }
}
