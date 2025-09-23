package com.roudane.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;


@SpringBootApplication
@EnableConfigServer
public class configServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(configServerApplication.class, args);
	}

}
