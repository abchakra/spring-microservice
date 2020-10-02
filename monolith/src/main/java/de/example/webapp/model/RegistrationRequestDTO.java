package de.example.webapp.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class RegistrationRequestDTO {
    @Valid
    @NotNull
    private UserDTO user;
    @Valid
    @NotNull
    private CustomerDTO customer;
    @Valid
    @NotNull
    private LoanDTO loan;
}
