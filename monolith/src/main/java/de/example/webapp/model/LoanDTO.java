package de.example.webapp.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoanDTO {
    private String id;
    @NotNull
    private Double amount;
    @NotNull
    private Integer duration;
    private LoanStatus status;
}
