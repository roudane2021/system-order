package com.roudane.order.domain_inventory.port.output;

import com.roudane.order.domain_inventory.model.InventoryModel;

import java.util.Set;

public interface InventoryPersistenceOutProt {

    InventoryModel updateOrder(final InventoryModel NotificationModel);
    Set<InventoryModel> listOrder();
    InventoryModel getOrder(final Long orderID);
    InventoryModel createOrder(final InventoryModel NotificationModel);
}
