package com.roudane.order.infra_notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.roudane.order.infra_notification", "com.roudane.order.domain_notification"}) // Added ComponentScan
public class InfraNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfraNotificationApplication.class, args);
	}

}
