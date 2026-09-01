package com.bank.cards.application.dto.input;

public record UpdateUserProfileCommand(
        String currentUsername,
        String newUsername,
        String newPassword,
        String newEmail
) {}