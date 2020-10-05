package com.example.customers.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.customers.model.Customer;
import com.example.customers.repository.CustomersRepository;

@Service
public class CustomersService {

	private static final Logger LOGGER = LogManager.getLogger(CustomersService.class);

	@Autowired
	private CustomersRepository customerRepository;

	public Customer save(Customer customer) {
		LOGGER.info("Saving new customer with userId:{}", customer.getUserId());
		return customerRepository.save(customer);
	}

	public Customer findById(Integer id) {
		LOGGER.info("Finding customer by id:{}", id);
		return customerRepository.findCustomerById(id);
	}
}
