package com.bank.cards.presentation.dto.request;

import com.bank.cards.domain.valueobject.ChangeRoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangeRoleRequest(
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 4, max = 50, message = "Username must be 4-50 characters")
        String username,

        @NotBlank(message = "Role cannot be blank")
        String role,

        @NotNull(message = "Operation type cannot be null")
        ChangeRoleType operationType
) {}