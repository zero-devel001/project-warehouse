package com.sharsheev.ewarehouse.model.exceptions;

public class InvalidCategoryIdException extends RuntimeException {
    public InvalidCategoryIdException() {
        super("Invalid Category Id Exception");
    }
}
