package com.sharsheev.ewarehouse.model.exceptions;

public class InvalidProductIdException extends RuntimeException {
    public InvalidProductIdException() {
        super("Invalid Item id Exception");
    }
}
