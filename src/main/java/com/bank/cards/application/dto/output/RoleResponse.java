package com.bank.cards.application.dto.output;

import com.bank.cards.domain.entity.Role;

import java.util.UUID;

public record RoleResponse(UUID id, String name) {
    public static RoleResponse from(Role role) {
        return new RoleResponse(
            role.getId() != null ? role.getId().value() : null,
            role.getName().value()
        );
    }
}