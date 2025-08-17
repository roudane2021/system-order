package com.roudane.inventory.domain.port.input;

public interface IUpdateStockUserCase {

    void adjustStock(String productId, int quantityChange, String reason);
}
