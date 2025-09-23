package com.roudane.order.infra_notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
public class InfraNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfraNotificationApplication.class, args);
	}

}
