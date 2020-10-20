package com.example.loanapplication.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.loanapplication.model.Customer;
import com.example.loanapplication.model.CustomerDto;
import com.example.loanapplication.model.LoanApplication;
import com.example.loanapplication.model.LoanApplicationDto;
import com.example.loanapplication.model.LoanApplicationsDto;
import com.example.loanapplication.service.LoanApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:test.properties")
public class LoanApplicationControllerTests {

	@MockBean
	private LoanApplicationService loanApplicationService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ModelMapper modelMapper;

	@Test
	@DisplayName("Test loanApplication found - GET /api/loanapplications?customerId=11")
	public void testGetLoanapplicationsByIdFindsCustomerId() throws Exception {

		// Prepare mock loanApplication
		int customerId = 11;
		Customer customer = new Customer(customerId, 1, "John", "Smith", "johnsmith@gmail.com", "+4917589");
		LoanApplication mockLoanApplication1 = new LoanApplication(4, customerId, 1000, 12, "APPLIED");
		LoanApplication mockLoanApplication2 = new LoanApplication(5, customerId, 2000, 24, "DENIED");
		LoanApplicationsDto responseLoanApplications = convertToDto(List.of(mockLoanApplication1, mockLoanApplication2),
				customer);

		// Prepare mocked service method
		doReturn(responseLoanApplications).when(loanApplicationService).findCustomerById(customerId);
		// Perform GET request
		ResultActions result = mockMvc.perform(get("/api/loanapplications/?customerId={id}", customerId))
				// Validate 200 OK and JSON response type received
				.andExpect(status().isOk());

		result.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

				// Validate response headers
				.andExpect(header().string(HttpHeaders.ETAG, "\"11\""))
				.andExpect(header().string(HttpHeaders.LOCATION, "/loanapplications?customerId=11"))

				// Validate response body
				.andExpect(jsonPath("$['customer'].id", is(11)))
				.andExpect(jsonPath("$['customer'].firstName", is("John")))
				.andExpect(jsonPath("$['customer'].lastName", is("Smith")))
				.andExpect(jsonPath("$['loans'][0].id", is(4))).andExpect(jsonPath("$['loans'][1].id", is(5)));

	}

	@Test
	@DisplayName("Add a new loanApplication - POST /api/loanapplications")
	public void testAddNewLoanapplication() throws Exception {
		// Prepare mock loanApplication
		LoanApplication newLoanApplication = new LoanApplication(11, 1000, 12);
		LoanApplication mockLoanApplication = new LoanApplication(2, 11, 1000, 12, "APPLIED");

		// Prepare mock service method
		doReturn(mockLoanApplication).when(loanApplicationService).save(ArgumentMatchers.any());

		// Perform POST request
		mockMvc.perform(post("/api/loanapplications").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(newLoanApplication)))

				// Validate 201 CREATED and JSON response type received
				.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

				// Validate response headers
				.andExpect(header().string(HttpHeaders.ETAG, "\"11\""))
				.andExpect(header().string(HttpHeaders.LOCATION, "/loanapplications/2"))

				// Validate response body
				.andExpect(jsonPath("$.id", is(2))).andExpect(jsonPath("$.amount", is(1000)))
				.andExpect(jsonPath("$.duration", is(12))).andExpect(jsonPath("$.status", is("APPLIED")))
				.andExpect(jsonPath("$.customerId", is(11)));
	}

	public LoanApplicationsDto convertToDto(List<LoanApplication> loans, Customer customer) {
		CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
		List<LoanApplicationDto> loanDtoList = loans.stream().map(this::convertToDto).collect(Collectors.toList());

		return new LoanApplicationsDto(customerDto, loanDtoList);

	}

	public LoanApplicationDto convertToDto(LoanApplication loan) {
		LoanApplicationDto loanApplicationDto = modelMapper.map(loan, LoanApplicationDto.class);
		return loanApplicationDto;
	}

}
