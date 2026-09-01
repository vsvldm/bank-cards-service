package com.bank.cards.application.usecase;

import com.bank.cards.application.dto.input.CreateRoleCommand;
import com.bank.cards.application.dto.output.RoleResponse;
import com.bank.cards.domain.entity.Role;
import com.bank.cards.domain.exception.RoleAlreadyExistsException;
import com.bank.cards.domain.repository.RoleRepository;
import com.bank.cards.domain.valueobject.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateRoleUseCase {
    private final RoleRepository roleRepository;

    @Transactional
    public RoleResponse execute(CreateRoleCommand command) {
        RoleName roleName = new RoleName(command.name());
        
        if (roleRepository.existsByName(roleName)) {
            throw new RoleAlreadyExistsException(roleName.value());
        }

        Role newRole = Role.createNew(roleName);
        Role savedRole = roleRepository.save(newRole);
        
        return RoleResponse.from(savedRole);
    }
}