package com.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = { "spring.cloud.config.enabled:true",
		"spring.profiles.active=native", "timezone = GMT" })
@ActiveProfiles("test")
class ConfigServerApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void defaultConfigurationAvailable() {
		ResponseEntity<Environment> entity = restTemplate
				.getForEntity("http://localhost:" + port + "/application/default", Environment.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
//		assertThat(entity.getBody().getPropertySources().isEmpty()).isFalse();
		// assertThat(entity.getBody().getPropertySources().get(0)).isNotNull();
		// assertThat(entity.getBody().getPropertySources().get(0).getSource().get("app.auth-server"))
		// .isEqualTo("localhost");
	}

	@Test
	void customerServiceConfigurationAvailable() {
		ResponseEntity<Environment> entity = restTemplate
				.getForEntity("http://localhost:" + port + "/application/customer-service", Environment.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		// assertThat(entity.getBody().getPropertySources().isEmpty()).isFalse();
	}
}