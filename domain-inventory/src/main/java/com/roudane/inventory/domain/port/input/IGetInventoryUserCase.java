package com.roudane.inventory.domain.port.input;

import com.roudane.inventory.domain.model.InventoryItem;

import java.util.List;
import java.util.Optional;

public interface IGetInventoryUserCase {

    Optional<InventoryItem> findInventoryByProductId(String productId);

    List<InventoryItem> findAllInventoryItems();
}
