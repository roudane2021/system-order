package com.roudane.inventory.infra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
// Scan for components in domain and infra layers. Adjust basePackages as needed.
@ComponentScan(basePackages = {"com.roudane.inventory.domain", "com.roudane.inventory.infra"})
// Scan for JPA entities, assuming InventoryItem might become an @Entity
@EntityScan(basePackages = {"com.roudane.inventory.domain.model"})
// Enable JPA repositories, assuming repository interfaces are in this path or sub-packages
@EnableJpaRepositories(basePackages = {"com.roudane.inventory.infra.persistence.repository"})
public class InventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }
}
