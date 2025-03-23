package com.roudane.order.domain_inventory.service;

import com.roudane.order.domain_inventory.exception.InventoryInvalidException;
import com.roudane.order.domain_inventory.model.InventoryModel;
import com.roudane.order.domain_inventory.port.input.ICreateInventoryUseCase;
import com.roudane.order.domain_inventory.port.input.IGetInventoryUseCase;
import com.roudane.order.domain_inventory.port.input.IListInventoryUseCase;
import com.roudane.order.domain_inventory.port.input.IUpdateInventoryUseCase;
import com.roudane.order.domain_inventory.port.output.ILoggerPort;
import com.roudane.order.domain_inventory.port.output.InventoryEventPublisherOutPort;
import com.roudane.order.domain_inventory.port.output.InventoryPersistenceOutProt;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
public class InventoryDomain implements ICreateInventoryUseCase, IUpdateInventoryUseCase,
        IGetInventoryUseCase, IListInventoryUseCase {

    private final InventoryEventPublisherOutPort eventPublisherOutPort;
    private final InventoryPersistenceOutProt persistenceOutProt;
    private final ILoggerPort loggerPort;

    /**
     * Crée un nouvel inventaire.
     *
     * @param inventoryModel Les données de l'inventaire à créer.
     * @return L'inventaire créé.
     */
    @Override
    public InventoryModel createInventory(final InventoryModel inventoryModel) {
        loggerPort.debug("Creating Inventory: " + inventoryModel);
        if (Objects.isNull(inventoryModel) || Objects.isNull(inventoryModel.getId())) {
            throw new InventoryInvalidException("inventoryMode is invalid");
        }
        // Enregistrement de la commande
        final InventoryModel orderModelSaved = persistenceOutProt.createOrder(inventoryModel);



        loggerPort.info("Order created successfully with ID: " + orderModelSaved.getId());
        return orderModelSaved;
    }

    /**
     * @param orderID
     * @return
     */
    @Override
    public InventoryModel getInventory(Long orderID) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public Set<InventoryModel> listInventory() {
        return null;
    }

    /**
     * @param NotificationModel
     * @return
     */
    @Override
    public InventoryModel updateInventory(InventoryModel NotificationModel) {
        return null;
    }
}
