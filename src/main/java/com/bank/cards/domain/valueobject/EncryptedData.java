package com.bank.cards.domain.valueobject;

public record EncryptedData(String value) {

    public EncryptedData {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Encrypted data cannot be empty");
        }
    }
}