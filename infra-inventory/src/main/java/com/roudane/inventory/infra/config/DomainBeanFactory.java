package com.roudane.inventory.infra.config;

import com.roudane.inventory.domain.repository.InventoryRepository;
import com.roudane.inventory.domain.service.InventoryService;
import com.roudane.inventory.domain.service.InventoryServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainBeanFactory {

    @Bean
    public InventoryService inventoryService(InventoryRepository inventoryRepository) {
        // LoggerFactory is available via slf4j-api from domain module
        return new InventoryServiceImpl(inventoryRepository);
    }
}
