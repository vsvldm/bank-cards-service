package com.bank.cards.application.dto.input;

public record RegisterUserCommand(
        String username,
        String password,
        String email
) {}