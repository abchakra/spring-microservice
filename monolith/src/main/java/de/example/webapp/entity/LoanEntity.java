package de.example.webapp.entity;

import de.example.webapp.model.LoanStatus;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "loan")
public class LoanEntity {
    @Id
    private String id;
    private Double amount;
    private Integer duration;
    private LoanStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;
}
