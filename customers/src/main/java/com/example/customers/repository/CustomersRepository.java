package com.example.customers.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.customers.model.Customer;

public interface CustomersRepository extends CrudRepository<Customer, Integer> {
    Customer findCustomerById(Integer id);
    Customer findCustomerByIdAndUserId(Integer id, Integer userId);
}
