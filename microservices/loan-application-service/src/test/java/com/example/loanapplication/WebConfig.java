package com.example.loanapplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

//the regular configuration not active with test profile
@Configuration
@Profile("test")
public class WebConfig {
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}