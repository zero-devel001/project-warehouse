package com.sharsheev.ewarehouse.model.exceptions;

public class InvalidOrderIdException extends RuntimeException {
    public InvalidOrderIdException() {
        super("Invalid Manufacturer id Exception");
    }
}
