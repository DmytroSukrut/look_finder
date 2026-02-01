package com.look_finder.errors;

public class UserIsntRegistratedException extends RuntimeException {
    public UserIsntRegistratedException() {
        super("User with this email is not registrated");
    }
}
