package com.bank.cards.domain.repository;

import com.bank.cards.domain.entity.Role;
import com.bank.cards.domain.valueobject.RoleId;
import com.bank.cards.domain.valueobject.RoleName;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {

    Role save(Role role);

    Optional<Role> findById(RoleId id);

    Optional<Role> findByName(RoleName name);

    boolean existsByName(RoleName name);

    void deleteById(RoleId id);
    
    List<Role> findAll(int page, int size, String sort);
}