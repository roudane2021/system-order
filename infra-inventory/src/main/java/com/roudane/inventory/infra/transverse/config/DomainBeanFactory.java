package com.roudane.inventory.infra.transverse.config;

import com.roudane.inventory.domain.port.output.event.IInventoryEventPublisherOutPort;
import com.roudane.inventory.domain.port.output.persistence.IInventoryPersistenceOutPort;
import com.roudane.inventory.domain.service.InventoryDomain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainBeanFactory {

    @Bean
    public InventoryDomain inventoryDomain(IInventoryPersistenceOutPort inventoryRepository, IInventoryEventPublisherOutPort eventPublisherPort) {
        return new InventoryDomain(inventoryRepository, eventPublisherPort);
    }
}
