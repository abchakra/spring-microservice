package com.example.loanapplication.service;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

import com.example.loanapplication.model.LoanApplication;
import com.example.loanapplication.model.LoanApplicationsDto;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import wiremock.org.apache.http.HttpResponse;
import wiremock.org.apache.http.client.ClientProtocolException;
import wiremock.org.apache.http.impl.client.CloseableHttpClient;
import wiremock.org.apache.http.impl.client.HttpClients;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(value = "classpath:test.properties")
@AutoConfigureWireMock(port = 0)
public class LoanApplicationServiceTests {
	@Autowired
	private LoanApplicationService loanApplicationService;
	@Value("${customerprovider.uri}")
	private String customerServiceURI;
	private WireMockServer wireMockServer;
	@Autowired
	private Environment environment;
	private CloseableHttpClient httpClient = HttpClients.createDefault();

	private static final String PATH_STRING = "api/customers/11";

//	@Autowired
//	private TestRestTemplate testRestTemplate;

	@Test
	public void shouldPopulateEnvironmentWithWiremockPort() {
		assertThat(environment.containsProperty("wiremock.server.port")).isTrue();
		assertThat(environment.getProperty("wiremock.server.port")).matches("\\d+");
	}

	@BeforeEach
	public void setupWireMockServer() {
		// initialize WireMock server
		wireMockServer = new WireMockServer(8080);
		wireMockServer.start();
		// configure response stub
		wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(PATH_STRING))
				.willReturn(aResponse().withStatus(200).withBodyFile("json/customers-response.json")));

	}

	@AfterEach
	public void stopWireMockServer() {
		wireMockServer.stop();
	}

	@Test
	public void testGetLoanApplicationSuccessfully() throws ClientProtocolException, IOException {

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
