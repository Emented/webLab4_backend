package com.emented.weblab4.exception;

public class UserDoesntHaveRoleException extends RuntimeException {

    public UserDoesntHaveRoleException(String message) {
        super(message);
    }
}
