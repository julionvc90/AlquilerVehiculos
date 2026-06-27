package com.example.multa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MultaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultaApplication.class, args);
	}

}
