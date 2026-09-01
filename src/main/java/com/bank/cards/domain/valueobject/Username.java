package com.bank.cards.domain.valueobject;

public record Username(String value) {
    
    public Username {
        if (value == null || value.length() < 4 || value.length() > 50) {
            throw new IllegalArgumentException("Username must be between 4 and 50 characters");
        }
    }
}