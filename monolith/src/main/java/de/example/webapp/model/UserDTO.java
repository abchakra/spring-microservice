package de.example.webapp.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {
    private String id;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private String role;
}
