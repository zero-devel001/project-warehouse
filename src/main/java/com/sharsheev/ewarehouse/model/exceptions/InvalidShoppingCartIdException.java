package com.sharsheev.ewarehouse.model.exceptions;

public class InvalidShoppingCartIdException extends RuntimeException {
    public InvalidShoppingCartIdException() {
        super("Invalid ShoppingCart id Exception");
    }
}
