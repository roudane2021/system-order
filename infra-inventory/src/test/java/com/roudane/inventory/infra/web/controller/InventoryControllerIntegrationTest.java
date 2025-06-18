package com.roudane.inventory.infra.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roudane.inventory.domain.model.InventoryItem;
import com.roudane.inventory.domain.service.IInventoryServiceInPort;
import com.roudane.inventory.infra.web.dto.InventoryItemDto;
import com.roudane.inventory.infra.web.dto.StockAdjustmentRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*; // For is() and hasSize()

// Using @SpringBootTest will load the full application context.
// For more focused controller tests, @WebMvcTest could be used,
// but then more mocking of dependencies might be needed if not using @MockBean.
@SpringBootTest
@AutoConfigureMockMvc
class InventoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Use MockBean to mock the service layer for controller tests
    private IInventoryServiceInPort inventoryServiceInPort;

    @Autowired
    private ObjectMapper objectMapper; // For converting objects to JSON strings

    private InventoryItem item1;
    private InventoryItem item2;
    private InventoryItemDto itemDto1;

    @BeforeEach
    void setUp() {
        item1 = new InventoryItem("prod1", 10);
        item2 = new InventoryItem("prod2", 5);
        itemDto1 = new InventoryItemDto("prod1", 10); // Assuming InventoryWebMapper works as tested
    }

    @Test
    void getInventoryByProductId_whenItemExists_shouldReturnItemDto() throws Exception {
        when(inventoryServiceInPort.findInventoryByProductId("prod1")).thenReturn(Optional.of(item1));

        mockMvc.perform(get("/api/v1/inventory/{productId}", "prod1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId", is("prod1")))
                .andExpect(jsonPath("$.quantity", is(10)));
    }

    @Test
    void getInventoryByProductId_whenItemDoesNotExist_shouldReturnNotFound() throws Exception {
        when(inventoryServiceInPort.findInventoryByProductId("prodNonExistent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/inventory/{productId}", "prodNonExistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllInventoryItems_shouldReturnListOfItemDtos() throws Exception {
        List<InventoryItem> items = Arrays.asList(item1, item2);
        when(inventoryServiceInPort.findAllInventoryItems()).thenReturn(items);

        mockMvc.perform(get("/api/v1/inventory"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productId", is("prod1")))
                .andExpect(jsonPath("$[1].productId", is("prod2")));
    }

    @Test
    void getAllInventoryItems_whenNoItems_shouldReturnEmptyList() throws Exception {
        when(inventoryServiceInPort.findAllInventoryItems()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/inventory"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void adjustStock_validRequest_shouldReturnOk() throws Exception {
        StockAdjustmentRequestDto adjustmentDto = new StockAdjustmentRequestDto(5, "Stock intake");
        // Mock the service call for adjustStock (it's void)
        doNothing().when(inventoryServiceInPort).adjustStock(anyString(), anyInt(), anyString());

        mockMvc.perform(post("/api/v1/inventory/{productId}/stock", "prod1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adjustmentDto)))
                .andExpect(status().isOk());

        verify(inventoryServiceInPort, times(1)).adjustStock("prod1", 5, "Stock intake");
    }

    // Example of how to test for an exception from the service layer resulting in an HTTP error
    // This requires the GlobalExceptionHandler or specific @ResponseStatus on exceptions
    // For now, this test assumes adjustStock might throw specific exceptions handled by Spring's default error handling
    // or a custom @ControllerAdvice not implemented in this script.
    // If InventoryDomainException from adjustStock (e.g. item not found) should result in 404 or 400:
    /*
    @Test
    void adjustStock_whenServiceThrowsDomainException_shouldReturnAppropriateError() throws Exception {
        StockAdjustmentRequestDto adjustmentDto = new StockAdjustmentRequestDto(5, "Test");
        doThrow(new com.roudane.inventory.domain.exception.InventoryDomainException("Item not found"))
            .when(inventoryServiceInPort).adjustStock("prodNotFound", 5, "Test");

        mockMvc.perform(post("/api/v1/inventory/{productId}/stock", "prodNotFound")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adjustmentDto)))
                .andExpect(status().isInternalServerError()); // Or isNotFound(), isBadRequest() if handled by ControllerAdvice
    }
    */
}
