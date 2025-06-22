package com.roudane.inventory.infra;

import com.roudane.inventory.infra.persistence.entity.InventoryItemEntity;
import com.roudane.inventory.infra.persistence.repository.JpaInventoryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@SpringBootApplication
// Scan for components in domain and infra layers. Adjust basePackages as needed.
@ComponentScan(basePackages = {"com.roudane.inventory.domain", "com.roudane.inventory.infra"})
// Scan for JPA entities, assuming InventoryItem might become an @Entity
@EntityScan(basePackages = {"com.roudane.inventory.infra.persistence.entity"})
// Enable JPA repositories, assuming repository interfaces are in this path or sub-packages
@EnableJpaRepositories(basePackages = {"com.roudane.inventory.infra.persistence.repository"})
@RequiredArgsConstructor
public class InventoryApplication   implements CommandLineRunner  {

   // implements CommandLineRunner
    private final JpaInventoryItemRepository jpaInventoryItemRepository;

    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        InventoryItemEntity item1 = InventoryItemEntity.builder()
                .productId("2001")
                .quantity(25878)
                .build();
        InventoryItemEntity item2 = InventoryItemEntity.builder()
                .productId("2002")
                .quantity(25874)
                .build();

       //jpaInventoryItemRepository.save(item1);
      //  jpaInventoryItemRepository.save(item2);
    }
}
