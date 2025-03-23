package com.roudane.order.domain_inventory.port.input;

import com.roudane.order.domain_inventory.model.InventoryModel;

public interface ICreateInventoryUseCase {

    InventoryModel createInventory(final InventoryModel NotificationModel);
}
