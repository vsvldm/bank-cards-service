package com.bank.cards.domain.entity;

import com.bank.cards.domain.exception.SystemRoleModificationException;
import com.bank.cards.domain.valueobject.RoleId;
import com.bank.cards.domain.valueobject.RoleName;

public class Role {

    public static final String DEFAULT_ROLE_NAME = "ROLE_USER";

    private final RoleId id;
    private RoleName name;

    private Role(RoleId id, RoleName name) {
        this.id = id;
        this.name = name;
    }

    public static Role createNew(RoleName name) {
        return new Role(RoleId.generate(), name);
    }
    public static Role reconstitute(RoleId id, RoleName name) {
        return new Role(id, name);
    }

    public void updateName(RoleName newName) {
        if (isDefaultRole()) {
            throw new SystemRoleModificationException("Default role cannot be modified");
        }
        this.name = newName;
    }

    public boolean isDefaultRole() {
        return this.name.value().equals(DEFAULT_ROLE_NAME);
    }

    public RoleId getId() { return id; }
    public RoleName getName() { return name; }
}