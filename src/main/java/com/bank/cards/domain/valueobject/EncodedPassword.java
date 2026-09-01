package com.bank.cards.domain.valueobject;

public record EncodedPassword(String value) {
    public EncodedPassword {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Encoded password cannot be empty");
        }
    }
}