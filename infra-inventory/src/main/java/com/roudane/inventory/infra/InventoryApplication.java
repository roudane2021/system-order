package com.roudane.inventory.infra;

import com.roudane.inventory.infra.transverse.config.datasource.OracleDataSourceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.roudane.inventory.domain", "com.roudane.inventory.infra"})
@EntityScan(basePackages = {"com.roudane.inventory.infra.persistence.entity"})
@EnableJpaRepositories(basePackages = {"com.roudane.inventory.infra.persistence.repository"})
@RequiredArgsConstructor
@EnableConfigurationProperties(OracleDataSourceProperties.class)
@EnableDiscoveryClient
public class InventoryApplication   implements CommandLineRunner  {

    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
