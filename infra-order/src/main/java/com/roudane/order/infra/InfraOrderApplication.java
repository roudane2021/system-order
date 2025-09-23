package com.roudane.order.infra;

import com.roudane.order.infra.transverse.config.datasource.OracleDataSourceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableConfigurationProperties(OracleDataSourceProperties.class)
@EnableDiscoveryClient
public class InfraOrderApplication {


	public static void main(String[] args) {
		SpringApplication.run(InfraOrderApplication.class, args);
	}

}
