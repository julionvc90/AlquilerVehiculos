package com.example.alquiler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AlquilerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlquilerApplication.class, args);
	}

}
