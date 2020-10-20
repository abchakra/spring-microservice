package com.example.loanapplication.integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.github.tomakehurst.wiremock.WireMockServer;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:test.properties")
public class LoanApplicationIntegrationTests {
	@Autowired
	private MockMvc mockMvc;

	private WireMockServer wireMockServer;

	@BeforeEach
	public void setupWireMockServer() {
		wireMockServer = new WireMockServer(8080);
		wireMockServer.start();
	}

	@AfterEach
	public void stopWireMockServer() {
		wireMockServer.stop();
	}

	@Test
	@DisplayName("Test loanApplication found - GET /api/loanapplications/?customerId=11")
	public void testGetLoanApplicationSuccessfully() throws Exception {
		// Perform the request
		ResultActions result = mockMvc.perform(get("/api/loanapplications/?customerId={id}", 11))
				// Validate status and content type
				.andExpect(status().isOk());
//		String content = result.andReturn().getResponse().getContentAsString();
//		System.out.println(content);
		result.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

				// Validate response headers
				.andExpect(header().string(HttpHeaders.ETAG, "\"11\""))
				.andExpect(header().string(HttpHeaders.LOCATION, "/loanapplications?customerId=11"))

				// Validate response body
				.andExpect(jsonPath("$['customer'].id", is(11)))
				.andExpect(jsonPath("$['customer'].firstName", is("John")))
				.andExpect(jsonPath("$['customer'].lastName", is("Smith")));
	}
}
