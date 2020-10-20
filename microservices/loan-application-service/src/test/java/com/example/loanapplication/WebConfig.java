package com.example.loanapplication;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
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
		// you can use your regular rest template here.
		return new RestTemplate();
	}
}