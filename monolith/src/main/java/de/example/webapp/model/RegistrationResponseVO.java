package de.example.webapp.model;

import lombok.Value;

@Value
public class RegistrationResponseVO {
    private String userId;
    private String loanId;
    private LoanStatus loanStatus;
}
