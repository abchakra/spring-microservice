package de.example.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.ArrayList;
import java.util.List;

@EnableOAuth2Client
@Configuration
public class OauthTestConfig {
    public static final String BASE_URL = "http://localhost:8080";
    public static final String TEST_USER = "testuser@gmail.com";
    public static final String TEST_PASSWORD = "test-password";
    private static final String TOKEN_URL = BASE_URL + "/oauth/token";

    @Bean
    protected OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails() {
        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
        List<String> scopes = new ArrayList<>(2);
        scopes.add("write");
        scopes.add("read");
        resource.setAccessTokenUri(TOKEN_URL);
        resource.setClientId("client");
        resource.setClientSecret("secret");
        resource.setGrantType("password");
        resource.setScope(scopes);
        resource.setUsername(TEST_USER);
        resource.setPassword(TEST_PASSWORD);
        return resource;
    }

    @Bean
    public OAuth2RestTemplate oAuth2RestTemplate(OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails) {
        AccessTokenRequest atr = new DefaultAccessTokenRequest();
        return new OAuth2RestTemplate(oAuth2ProtectedResourceDetails, new DefaultOAuth2ClientContext(atr));
    }
}
