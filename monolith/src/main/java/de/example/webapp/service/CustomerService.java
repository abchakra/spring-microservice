package de.example.webapp.service;

import de.example.webapp.entity.CustomerEntity;
import de.example.webapp.entity.UserEntity;
import de.example.webapp.exception.UserNotFoundException;
import de.example.webapp.model.CustomerDTO;
import de.example.webapp.model.UserDTO;
import de.example.webapp.repository.CustomerRepository;
import de.example.webapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Transactional
    public CustomerDTO create(CustomerDTO customerDTO, UserDTO userDTO) {
        Optional<UserEntity> optionalUser = userRepository.findById(userDTO.getId());
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException();
        }
        CustomerEntity customer = new CustomerEntity();
        BeanUtils.copyProperties(customerDTO, customer);
        customer.setUser(optionalUser.get());
        customerRepository.save(customer);
        log.info("Saved customer with id: {}", customer.getId());
        customerDTO.setId(customer.getId());
        return customerDTO;
    }
}
