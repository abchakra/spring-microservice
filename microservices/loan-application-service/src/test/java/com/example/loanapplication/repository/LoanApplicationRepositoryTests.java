package com.example.loanapplication.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.loanapplication.model.LoanApplication;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith({ SpringExtension.class })
@TestPropertySource(value = "classpath:test.properties")
@SpringBootTest
public class LoanApplicationRepositoryTests {
	@Autowired
	private LoanApplicationRepository loanApplicationRepository;

	private static File DATA_JSON = Paths.get("src", "test", "resources", "mockData.json").toFile();

	@BeforeEach
	public void setup() throws IOException {
		// Deserialize products from JSON file to LoanApplication array
		LoanApplication[] loanApplications = new ObjectMapper().readValue(DATA_JSON, LoanApplication[].class);

		// Save each LoanApplication to database
		Arrays.stream(loanApplications).forEach(loanApplicationRepository::save);
	}

	@AfterEach
	public void cleanup() {
		// Cleanup the database after each test
		loanApplicationRepository.deleteAll();
	}

	@Test
	@DisplayName("Test LoanApplication found with  existing customer id")
	public void testLoanApplicationFoundForExistingCustomerId() {
		// Given two products in the database

		// When
		List<LoanApplication> retrievedLoanApplications = loanApplicationRepository
				.findLoanApplicationsByCustomerId(11);

		// Then
		Assertions.assertEquals(retrievedLoanApplications.size(), 2,
				"Two LoanApplication with CustomerId 11 should exist");
	}

	@Test
	@DisplayName("Test LoanApplication not found with non-existing customer id")
	public void testLoanApplicationNotFoundForNonExistingCustomerId() {
		// Given two products in the database

		// When
		List<LoanApplication> retrievedLoanApplications = loanApplicationRepository
				.findLoanApplicationsByCustomerId(100);

		// Then
		Assertions.assertEquals(retrievedLoanApplications.size(), 0,
				"LoanApplication with CustomerId 100 should not exist");
	}

	@Test
	@DisplayName("Test LoanApplication saved successfully")
	public void testLoanApplicationSavedSuccessfully() {
		// Prepare mock product
		LoanApplication newCustomer = new LoanApplication(10, 11, 1000, 12, "APPLIED");

		// When
		LoanApplication savedCustomer = loanApplicationRepository.save(newCustomer);

		// Then
		Assertions.assertNotNull(savedCustomer, "LoanApplication should be saved");
		Assertions.assertNotNull(savedCustomer.getId(), "LoanApplication should have an id when saved");
		Assertions.assertEquals(newCustomer.getStatus(), savedCustomer.getStatus());
	}
}
