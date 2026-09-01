package com.bank.cards.domain.valueobject;

public record RoleName(String value) {
    public RoleName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Role name cannot be empty");
        }

        String formatted = value.toUpperCase();

        if (!formatted.startsWith("ROLE_")) {
            formatted = "ROLE_" + formatted;
        }

        value = formatted;
    }
}