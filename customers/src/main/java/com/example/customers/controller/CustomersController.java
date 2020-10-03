package com.example.customers.controller;



import java.net.URI;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.customers.model.Customer;
import com.example.customers.service.CustomersService;

@RestController
public class CustomersController {
	private static final Logger LOGGER = LogManager.getLogger(CustomersController.class);

    @Autowired
    private CustomersService customerService;

    /**
     * Get the Customer with specified ID
     * @param   id ID of the Customer to get
     * @return  ResponseEntity with the found Customer
     *          or NOT_FOUND if no Customer found
     */
    @GetMapping("/customers/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Integer id){
        Customer customer = customerService.findById(id);
        if (customer != null){
            try {
                return ResponseEntity
                        .ok()
                        .eTag(Integer.toString(customer.getUserId()))
                        .location(new URI("/customers/" + customer.getId()))
                        .body(customer);
            } catch (URISyntaxException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    /**
     * Saves a new Customer
     * @param   customer Customer to save
     * @return  ResponseEntity with the saved Customer
     */
    @PostMapping("/customers")
    public ResponseEntity<?> saveProduct(@RequestBody Customer customer){
        LOGGER.info("Adding new customer with userId:{}", customer.getUserId());
        Customer newCustomer = customerService.save(customer);
        try {
            return ResponseEntity
                    .created(new URI("/customers/" + newCustomer.getId()))
                    .eTag(Integer.toString(newCustomer.getUserId()))
                    .body(newCustomer);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
}
