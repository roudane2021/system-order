package com.roudane.order.domain_inventory.port.input;

import com.roudane.order.domain_inventory.model.InventoryModel;

import java.util.Set;

public interface IListInventoryUseCase {
    Set<InventoryModel> listInventory();
}
