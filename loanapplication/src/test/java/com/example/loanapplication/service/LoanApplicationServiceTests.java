package com.example.loanapplication.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.example.loanapplication.model.LoanApplication;
import com.example.loanapplication.model.LoanApplicationsDto;
import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@TestPropertySource(value = "classpath:test.properties")
public class LoanApplicationServiceTests {
	@Autowired
	private LoanApplicationService loanApplicationService;

	private WireMockServer wireMockServer;

	@BeforeEach
	public void setupWireMockServer() {
		// initialize WireMock server
		wireMockServer = new WireMockServer(8081);
		wireMockServer.start();

		// configure response stub
		wireMockServer.stubFor(get(urlEqualTo("/api/customers/11"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withStatus(200)
						.withBodyFile("json/customers-response.json")));
	}

	@AfterEach
	public void stopWireMockServer() {
		wireMockServer.stop();
	}

	@Test
	public void testGetLoanApplicationSuccessfully() {
		LoanApplicationsDto loanApplicationsDto = loanApplicationService.findCustomerById(11);

		Assertions.assertNotNull(loanApplicationsDto, "LoanApplications should exist");
	}

	@Test
	public void testCreateNewLoanApplicationSuccessful() {

		LoanApplication newLoanApplication = new LoanApplication(11, 1000, 12);

		LoanApplication loanApplication = loanApplicationService.save(newLoanApplication);

		Assertions.assertEquals(loanApplication.getCustomerId(), newLoanApplication.getCustomerId(),
				"Customer ID should be 11");
	}
}
