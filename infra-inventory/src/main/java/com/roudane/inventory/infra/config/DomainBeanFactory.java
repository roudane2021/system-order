package com.roudane.inventory.infra.config;

import com.roudane.inventory.domain.port.out.IInventoryEventPublisherOutPort;
import com.roudane.inventory.domain.repository.IInventoryRepositoryPort;
import com.roudane.inventory.domain.service.IInventoryServiceInPort;
import com.roudane.inventory.domain.service.InventoryService; // Updated Impl name
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainBeanFactory {

    @Bean
    public IInventoryServiceInPort inventoryService(IInventoryRepositoryPort inventoryRepository, IInventoryEventPublisherOutPort eventPublisherPort) {
        // LoggerFactory is available via slf4j-api from domain module
        return new InventoryService(inventoryRepository, eventPublisherPort); // Updated Impl name
    }
}
