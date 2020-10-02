package de.example.webapp;

import de.example.webapp.config.OauthTestConfig;
import de.example.webapp.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RegistrationIT {
    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    @Test
    public void testValidInput() {
        RestTemplate restTemplate = new RestTemplate();
        RegistrationRequestDTO registrationRequest = new RegistrationRequestDTO();
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(OauthTestConfig.TEST_USER);
        userDTO.setPassword(OauthTestConfig.TEST_PASSWORD);
        registrationRequest.setUser(userDTO);
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setAmount(10000.00);
        loanDTO.setDuration(12);
        registrationRequest.setLoan(loanDTO);
        CustomerDTO customerDTO = new CustomerDTO();
        registrationRequest.setCustomer(customerDTO);
        ResponseEntity<RegistrationResponseTestDTO> responseEntity = restTemplate.postForEntity(
                OauthTestConfig.BASE_URL + "/register", registrationRequest, RegistrationResponseTestDTO.class);
        log.info("responseEntity: {}", responseEntity);
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
        RegistrationResponseTestDTO registrationResponseTestDTO = responseEntity.getBody();
        Assert.assertNotNull(registrationResponseTestDTO.getUserId());
        Assert.assertNotNull(registrationResponseTestDTO.getLoanId());
        Assert.assertNotNull(registrationResponseTestDTO.getLoanStatus());
        Assert.assertEquals(LoanStatus.CREATED, registrationResponseTestDTO.getLoanStatus());

        UserSearchRequestDTO userSearchRequestDTO = new UserSearchRequestDTO();
        userSearchRequestDTO.setBelowAmount(10000.01);

        ResponseEntity<UserDTO> searchResponseEntity = oAuth2RestTemplate.postForEntity(
                OauthTestConfig.BASE_URL + "/api/users/search", userSearchRequestDTO, UserDTO.class);
        log.info("searchResponseEntity: {}", searchResponseEntity);
    }
}
