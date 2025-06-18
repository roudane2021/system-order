package com.roudane.inventory.infra.web.controller;

import com.roudane.inventory.domain.model.InventoryItem;
import com.roudane.inventory.domain.service.IInventoryServiceInPort;
import com.roudane.inventory.infra.web.dto.InventoryItemDto;
import com.roudane.inventory.infra.web.dto.StockAdjustmentRequestDto;
import com.roudane.inventory.infra.web.mapper.InventoryWebMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException; // For simple not found

import java.util.List;
import java.util.Optional; // Assuming service might return Optional

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final IInventoryServiceInPort inventoryServiceInPort;
    private final InventoryWebMapper inventoryWebMapper;

    public InventoryController(IInventoryServiceInPort inventoryServiceInPort, InventoryWebMapper inventoryWebMapper) {
        this.inventoryServiceInPort = inventoryServiceInPort;
        this.inventoryWebMapper = inventoryWebMapper;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryItemDto> getInventoryByProductId(@PathVariable String productId) {
        // Assuming IInventoryServiceInPort will have a method like findByProductId
        // This method needs to be added to IInventoryServiceInPort and its implementation
        Optional<InventoryItem> itemOptional = inventoryServiceInPort.findInventoryByProductId(productId);

        return itemOptional
                .map(inventoryWebMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory item not found for product: " + productId));
    }

    @GetMapping
    public ResponseEntity<List<InventoryItemDto>> getAllInventoryItems() {
        // Assuming IInventoryServiceInPort will have a method like findAllInventoryItems
        // This method needs to be added to IInventoryServiceInPort and its implementation
        List<InventoryItem> items = inventoryServiceInPort.findAllInventoryItems();
        return ResponseEntity.ok(inventoryWebMapper.toDtoList(items));
    }

    @PostMapping("/{productId}/stock")
    public ResponseEntity<Void> adjustStock(
            @PathVariable String productId,
            @RequestBody StockAdjustmentRequestDto adjustmentRequest) {
        // Assuming IInventoryServiceInPort will have a method like adjustStockQuantity
        // This method needs to be added to IInventoryServiceInPort and its implementation
        // It might take productId, quantityChange (or newQuantity), and reason.
        inventoryServiceInPort.adjustStock(
            productId,
            adjustmentRequest.getQuantity(),
            adjustmentRequest.getReason()
        );
        return ResponseEntity.ok().build();
    }
}
