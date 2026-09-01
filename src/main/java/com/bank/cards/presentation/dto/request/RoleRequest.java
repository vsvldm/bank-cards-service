package com.bank.cards.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RoleRequest(
        @NotBlank(message = "Role name is required")
        @Size(min = 3,
            max = 20, 
            message = "Role name must be between 3 and 20 characters"
        )
        @Pattern(
            regexp = "^[a-zA-Z_]+$", 
            message = "Only letters and underscores allowed"
        )
        String name
) {}