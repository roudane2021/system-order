package com.roudane.inventory.infra.web.impl;

import com.roudane.inventory.domain.model.InventoryItem;
import com.roudane.inventory.domain.service.InventoryDomain;
import com.roudane.inventory.infra.web.IInventoryController;
import com.roudane.inventory.infra.web.dto.InventoryItemDto;
import com.roudane.inventory.infra.web.dto.StockAdjustmentRequestDto;
import com.roudane.inventory.infra.web.mapper.InventoryWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryControllerImpl implements IInventoryController {

    private final InventoryDomain inventoryDomain;



    @Override
    @GetMapping("/{productId}")
    public ResponseEntity<InventoryItemDto> getInventoryByProductId(@PathVariable String productId) {
        Optional<InventoryItem> itemOptional = inventoryDomain.findInventoryByProductId(productId);

        return itemOptional
                .map(InventoryWebMapper.INSTANCE::toDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory item not found for product: " + productId));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<InventoryItemDto>> getAllInventoryItems() {
        List<InventoryItem> items = inventoryDomain.findAllInventoryItems();
        return ResponseEntity.ok(InventoryWebMapper.INSTANCE.toDtoList(items));
    }

    @Override
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
