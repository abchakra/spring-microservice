package de.example.webapp.model;

import lombok.Data;

@Data
public class UserSearchRequestDTO {
    private Double belowAmount;
    private Double aboveAmount;
    // Can be extended for optional search parameters
}
