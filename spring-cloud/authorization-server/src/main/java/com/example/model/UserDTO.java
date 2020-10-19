package com.example.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserDTO {
    private String id;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String roles;
}
