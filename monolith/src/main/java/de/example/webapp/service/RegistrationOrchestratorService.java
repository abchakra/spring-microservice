package de.example.webapp.service;

import de.example.webapp.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationOrchestratorService {
    private final UserService userService;
    private final CustomerService customerService;
    private final LoanService loanService;

    @Transactional
    public RegistrationResponseVO register(RegistrationRequestDTO registrationRequest) {
        log.info("Entering register: loan: {}", registrationRequest.getLoan());
        UserDTO userDTO = userService.create(registrationRequest.getUser());
        CustomerDTO customerDTO = customerService.create(registrationRequest.getCustomer(), userDTO);
        log.info("Customer Saved: customerDTO: {}", customerDTO.getId());
        LoanDTO loanDTO = loanService.create(registrationRequest.getLoan(), customerDTO);
        return new RegistrationResponseVO(userDTO.getId(), loanDTO.getId(), loanDTO.getStatus());
    }
}
