package com.example.customers.integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.customers.model.Customer;
import com.example.customers.repository.CustomersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"eureka.client.enabled=false", "spring.cloud.config.enabled=false"})
@AutoConfigureMockMvc
public class CustomersServiceIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CustomersRepository customerRepository;

	File DATA_JSON = Paths.get("src", "test", "resources", "mockData.json").toFile();

	@BeforeEach
	public void setup() throws IOException {
		// Deserialize customers from JSON file to Customer array
		Customer[] customers = new ObjectMapper().readValue(DATA_JSON, Customer[].class);

		// Save each customer to database
		Arrays.stream(customers).forEach(customerRepository::save);
	}

	@AfterEach
	public void cleanup() {
		// Cleanup the database after each test
		customerRepository.deleteAll();
	}

	@Test
	@DisplayName("Test customer found - GET /api/customers/4")
	public void testGetProductByIdFindsCustomer() throws Exception {
		// Perform GET request
		mockMvc.perform(get("/api/customers/{id}", 1))
				// Validate 200 OK and JSON response type received
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

				// Validate response headers
				.andExpect(header().string(HttpHeaders.ETAG, "\"11\""))
				.andExpect(header().string(HttpHeaders.LOCATION, "/customers/1"))

				// Validate response body
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.userId", is(11)))
				.andExpect(jsonPath("$.firstName", is("John"))).andExpect(jsonPath("$.lastName", is("Smith")))
				.andExpect(jsonPath("$.email", is("johnsmith@example.com")))
				.andExpect(jsonPath("$.phone", is("+49 123 456 78 910")));

	}

	@Test
	@DisplayName("Add a new customer - POST /api/customers")
	public void testAddNewCustomer() throws Exception {
		// Prepare customer to save
		Customer newProduct = new Customer(12, "Jack", "Bower", "jackbower@gmail.com", "+497787979");

		// Perform POST request
		mockMvc.perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(newProduct)))

				// Validate 201 CREATED and JSON response type received
				.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

				// Validate response headers
				.andExpect(header().string(HttpHeaders.ETAG, "\"12\""))
				.andExpect(header().string(HttpHeaders.LOCATION, "/customers/5"))

				// Validate response body
				.andExpect(jsonPath("$.id", is(5))).andExpect(jsonPath("$.userId", is(12)))
				.andExpect(jsonPath("$.firstName", is("Jack"))).andExpect(jsonPath("$.lastName", is("Bower")))
				.andExpect(jsonPath("$.email", is("jackbower@gmail.com")))
				.andExpect(jsonPath("$.phone", is("+497787979")));
	}

}
