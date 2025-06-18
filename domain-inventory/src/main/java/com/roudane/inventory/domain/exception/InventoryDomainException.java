package com.roudane.inventory.domain.exception;

public class InventoryDomainException extends RuntimeException {
    public InventoryDomainException(String message) {
        super(message);
    }

    public InventoryDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
