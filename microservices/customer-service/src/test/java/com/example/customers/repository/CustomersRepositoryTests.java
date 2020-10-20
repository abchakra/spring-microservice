package com.example.customers.repository;

import com.example.customers.model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

@ExtendWith({ SpringExtension.class })
@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"eureka.client.enabled=false", "spring.cloud.config.enabled=false"})
public class CustomersRepositoryTests {

	@Autowired
	private CustomersRepository customerRepository;

	private static File DATA_JSON = Paths.get("src", "test", "resources", "mockData.json").toFile();

	@BeforeEach
	public void setup() throws IOException {
		// Deserialize products from JSON file to Customer array
		Customer[] customers = new ObjectMapper().readValue(DATA_JSON, Customer[].class);

		// Save each product to database
		Arrays.stream(customers).forEach(customerRepository::save);
	}

	@AfterEach
	public void cleanup() {
		// Cleanup the database after each test
		customerRepository.deleteAll();
	}

	@Test
	@DisplayName("Test product not found with non-existing id")
	public void testCustomerNotFoundForNonExistingId() {
		// Given two products in the database

		// When
		Customer retrievedCustomer = customerRepository.findCustomerById(100);

		// Then
		Assertions.assertNull(retrievedCustomer, "Customer with id 100 should not exist");
	}

	@Test
	@DisplayName("Test customer saved successfully")
	public void testCustomerSavedSuccessfully() {
		// Prepare mock product
		Customer newCustomer = new Customer(11, "John", "Smith", "johnsmith@gmail.com", "+4917589");

		// When
		Customer savedCustomer = customerRepository.save(newCustomer);

		// Then
		Assertions.assertNotNull(savedCustomer, "Customer should be saved");
		Assertions.assertNotNull(savedCustomer.getId(), "Customer should have an id when saved");
		Assertions.assertEquals(newCustomer.getEmail(), savedCustomer.getEmail());
	}

	@Test
	@DisplayName("Test Customer updated successfully")
	public void testCustomerUpdatedSuccessfully() {
		// Prepare the Customer
		Customer customerToUpdate = new Customer(11, "John", "Smith", "johnsmith@gmail.com", "+4917589");

		// When
		Customer updatedCustomer = customerRepository.save(customerToUpdate);

		// Then
		Assertions.assertEquals(customerToUpdate.getEmail(), updatedCustomer.getEmail());
		Assertions.assertEquals("John", updatedCustomer.getFirstName());
		Assertions.assertEquals("Smith", updatedCustomer.getLastName());
	}

	@Test
	@DisplayName("Test Customer deleted successfully")
	public void testProductDeletedSuccessfully() {
		// Given two Customers in the database

//		Iterable<Customer> customers = customerRepository.findAll();
		// When
//		Customer retrievedCustomer = customerRepository.findCustomerById(6);
//
//		// Then
//		Assertions.assertNotNull(retrievedCustomer, "Customer with id 6 should exist");

		// When
		customerRepository.deleteById(6);

		// Then
		Assertions.assertEquals(1L, customerRepository.count());
	}
}
