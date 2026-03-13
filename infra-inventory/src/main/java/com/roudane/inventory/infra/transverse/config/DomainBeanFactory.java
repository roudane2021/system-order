package com.roudane.inventory.infra.transverse.config;

import com.roudane.inventory.domain.port.output.json.IJsonOutPort;
import com.roudane.inventory.domain.port.output.persistence.IInventoryPersistenceOutPort;
import com.roudane.inventory.domain.port.output.persistence.IOutBoxPersistenceOutPort;
import com.roudane.inventory.domain.service.InventoryDomain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainBeanFactory {

    @Bean
    public InventoryDomain inventoryDomain(IInventoryPersistenceOutPort inventoryRepository,
                                           IOutBoxPersistenceOutPort outBoxPersistenceOutPort,
                                           IJsonOutPort jsonOutPort) {
        return new InventoryDomain(inventoryRepository, outBoxPersistenceOutPort, jsonOutPort);
    }
}
