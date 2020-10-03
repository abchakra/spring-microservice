package com.example.customers.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.customers.model.Customer;
import com.example.customers.service.CustomersService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CustomersControllerTest {

    @MockBean
    private CustomersService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test customer found - GET /api/customers/1")
    public void testGetCustomerByIdFindsCustomer() throws Exception {
        // Prepare mock customer
        Customer mockProduct = new Customer(1,11, "John", "Smith", "johnsmith@gmail.com", "+4917589");

        // Prepare mocked service method
        doReturn(mockProduct).when(customerService).findById(mockProduct.getId());

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/{id}", 1))
                // Validate 200 OK and JSON response type received
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

                // Validate response headers
                .andExpect(header().string(HttpHeaders.ETAG,  "\"11\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/customers/1"))

                // Validate response body
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(11)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Smith")))
                .andExpect(jsonPath("$.email", is("johnsmith@gmail.com")))
                .andExpect(jsonPath("$.phone", is("+4917589")));
    }


    @Test
    @DisplayName("Add a new customer - POST /api/customers")
    public void testAddNewCustomer() throws Exception {
        // Prepare mock customer
        Customer newCustomer = new Customer(12, "Jack", "Bower", "jackbower@gmail.com", "+497787979");
        Customer mockCustomer = new Customer(2,12, "Jack", "Bower", "jackbower@gmail.com", "+497787979");

        // Prepare mock service method
        doReturn(mockCustomer).when(customerService).save(ArgumentMatchers.any());

        // Perform POST request
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(newCustomer)))

                // Validate 201 CREATED and JSON response type received
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

                // Validate response headers
                .andExpect(header().string(HttpHeaders.ETAG, "\"12\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/customers/2"))

                // Validate response body
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.userId", is(12)))
                .andExpect(jsonPath("$.firstName", is("Jack")))
                .andExpect(jsonPath("$.lastName", is("Bower")))
                .andExpect(jsonPath("$.email", is("jackbower@gmail.com")))
                .andExpect(jsonPath("$.phone", is("+497787979")));
    }


}

