package com.example.demo.config;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingConfiguration {

	@Bean
	public WebProperties.Resources getWebPropertyResources(){
		return new WebProperties.Resources();
	}
}
