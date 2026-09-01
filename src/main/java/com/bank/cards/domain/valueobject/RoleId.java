package com.bank.cards.domain.valueobject;

import java.util.UUID;

public record RoleId(UUID value) {
    public RoleId {
        if (value == null) {
            throw new IllegalArgumentException("RoleId cannot be null");
        }
    }

    public static RoleId generate() {
        return new RoleId(UUID.randomUUID());
    }

    public static RoleId fromString(String id) {
        return new RoleId(UUID.fromString(id));
    }
}