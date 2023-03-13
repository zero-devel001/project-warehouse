package com.sharsheev.ewarehouse.model.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found exception");
    }
}
