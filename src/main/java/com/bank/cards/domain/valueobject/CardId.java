package com.bank.cards.domain.valueobject;

import java.util.UUID;

public record CardId(UUID value) {

    public CardId {
        if (value == null) {
            throw new IllegalArgumentException("CardId cannot be null");
        }
    }

    public static CardId generate() {
        return new CardId(UUID.randomUUID());
    }

    public static CardId from(UUID value) {
        return new CardId(value);
    }

    public static CardId fromString(String value) {
        return new CardId(UUID.fromString(value));
    }
}