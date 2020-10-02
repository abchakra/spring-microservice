package de.example.webapp.model;

import lombok.Data;

@Data
public class RegistrationResponseTestDTO {
    private String userId;
    private String loanId;
    private LoanStatus loanStatus;
}
