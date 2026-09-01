package com.bank.cards.application.dto.input;

public record ChangeUserRoleCommand(
        String initiatorUsername,
        String targetUsername,
        String roleName,
        boolean isAdd
) {}