package com.roudane.inventory.infra.web.controller;

import com.roudane.inventory.domain.model.InventoryItem;
import com.roudane.inventory.domain.service.InventoryDomain;
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

    private final InventoryDomain inventoryDomain;


    public InventoryController(InventoryDomain inventoryDomain) {
        this.inventoryDomain = inventoryDomain;
    }


    @GetMapping("/{productId}")
    public ResponseEntity<InventoryItemDto> getInventoryByProductId(@PathVariable String productId) {
        Optional<InventoryItem> itemOptional = inventoryDomain.findInventoryByProductId(productId);

        return itemOptional
                .map(InventoryWebMapper.INSTANCE::toDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory item not found for product: " + productId));
    }

    @GetMapping
    public ResponseEntity<List<InventoryItemDto>> getAllInventoryItems() {
        List<InventoryItem> items = inventoryDomain.findAllInventoryItems();
        return ResponseEntity.ok(InventoryWebMapper.INSTANCE.toDtoList(items));
    }

    @PostMapping("/{productId}/stock")
    public ResponseEntity<Void> adjustStock(
            @PathVariable String productId,
            @RequestBody StockAdjustmentRequestDto adjustmentRequest) {

        inventoryDomain.adjustStock(
            productId,
            adjustmentRequest.getQuantity(),
            adjustmentRequest.getReason()
        );
        return ResponseEntity.ok().build();
    }
}
