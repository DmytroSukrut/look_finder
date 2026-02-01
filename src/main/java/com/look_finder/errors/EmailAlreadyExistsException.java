package com.look_finder.errors;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("User with this email already exists");
    }
}
