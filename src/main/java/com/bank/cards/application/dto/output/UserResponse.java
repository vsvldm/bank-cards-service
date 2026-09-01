package com.bank.cards.application.dto.output;

import com.bank.cards.domain.entity.User;

import java.util.UUID;

public record UserResponse(UUID id,
                           String username,
                           String email
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId() != null ? user.getId().value() : null,
                user.getUsername().value(),
                user.getEmail().value()
        );
    }
}