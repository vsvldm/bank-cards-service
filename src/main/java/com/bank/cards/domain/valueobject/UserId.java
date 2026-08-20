package com.bank.cards.domain.valueobject;

public record UserId(Long value) {

    public UserId {
        if (value == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("UserId must be positive");
        }
    }
}