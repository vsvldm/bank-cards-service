package com.bank.cards.domain.entity;

import com.bank.cards.domain.exception.UserOperationException;
import com.bank.cards.domain.valueobject.Email;
import com.bank.cards.domain.valueobject.EncodedPassword;
import com.bank.cards.domain.valueobject.UserId;
import com.bank.cards.domain.valueobject.Username;
import java.util.HashSet;
import java.util.Set;

public class User {

    private final UserId id;
    private Username username;
    private EncodedPassword password;
    private Email email;
    private final Set<Role> roles;

    private User(UserId id, Username username,
                 EncodedPassword password, Email email, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = new HashSet<>(roles);
    }

    public static User createNew(Username username, EncodedPassword password,
                                 Email email, Role defaultRole) {
        Set<Role> initialRoles = new HashSet<>();
        initialRoles.add(defaultRole);
        return new User(UserId.generate(), username, password, email, initialRoles);
    }

    public static User reconstitute(UserId id, Username username,
                                    EncodedPassword password, Email email, Set<Role> roles) {
        return new User(id, username, password, email, roles);
    }

    public void updateProfile(Username username, EncodedPassword password, Email email) {
        if (username != null) this.username = username;
        if (password != null) this.password = password;
        if (email != null) this.email = email;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        if (role.isDefaultRole()) {
            throw new UserOperationException("User cannot remove default role");
        }
        this.roles.removeIf(r -> r.getName().value().equals(role.getName().value()));
    }

    public UserId getId() { return id; }
    public Username getUsername() { return username; }
    public EncodedPassword getPassword() { return password; }
    public Email getEmail() { return email; }
    public Set<Role> getRoles() { return new HashSet<>(roles); }
}