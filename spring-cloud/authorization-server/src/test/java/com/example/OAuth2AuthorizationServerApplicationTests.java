package com.example;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Tests for {@link OAuth2AuthorizationServerApplication}
 *
 * @author Josh Cummings
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = { "eureka.client.enabled=false" })
@AutoConfigureMockMvc
public class OAuth2AuthorizationServerApplicationTests {
	private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
	// clientId
	final static String CLIENT_ID = "client";
	// clientSecret
	final static String CLIENT_SECRET = "secret";
	final static String USERNAME = "john";
	final static String PASSWORD = "john";

	@Autowired
	MockMvc mockMvc;

	@Test
	public void requestTokenWhenUsingPasswordGrantTypeThenOk() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "password");
		params.add("client_id", "client");
		params.add("username", "john");
		params.add("password", "john");
//		client $2a$10$HlzMvb9OQayvlWfX5pzJTeZvH30eKwnc6gE0C3idjDvbQt4XJMTki
//		this.mvc.perform(post("/oauth/token").params(params) .with(httpBasic("client", "secret")).andExpect(status().isOk());
		mockMvc.perform(
				post("/oauth/token").params(params).with(httpBasic(CLIENT_ID, CLIENT_SECRET)).accept(CONTENT_TYPE))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

	}

	@Test
	public void requestJwkSetWhenUsingDefaultsThenOk() throws Exception {

		this.mockMvc.perform(get("/.well-known/jwks.json")).andExpect(status().isOk());
	}

}