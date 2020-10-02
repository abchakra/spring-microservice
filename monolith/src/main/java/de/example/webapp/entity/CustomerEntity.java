package de.example.webapp.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "customer")
public class CustomerEntity {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private UserEntity user;
}
