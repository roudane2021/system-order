package com.roudane.order.domain_inventory.port.input;

import com.roudane.order.domain_inventory.model.InventoryModel;

public interface IGetInventoryUseCase {

    InventoryModel getInventory(final Long orderID);
}
