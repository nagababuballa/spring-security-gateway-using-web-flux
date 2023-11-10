package com.example.authenticationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringWebfluxJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxJwtApplication.class, args);
	}

}
