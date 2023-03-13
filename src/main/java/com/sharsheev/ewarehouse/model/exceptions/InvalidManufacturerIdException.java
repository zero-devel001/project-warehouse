package com.sharsheev.ewarehouse.model.exceptions;

public class InvalidManufacturerIdException extends RuntimeException {
    public InvalidManufacturerIdException() {
        super("Invalid Manufacturer id Exception");
    }
}
