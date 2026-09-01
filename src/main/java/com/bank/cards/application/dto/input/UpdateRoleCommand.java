package com.bank.cards.application.dto.input;

import com.bank.cards.domain.valueobject.RoleId;

public record UpdateRoleCommand(
    RoleId id, 
    String newName
) {}