package com.bank.cards.application.usecase;

import com.bank.cards.application.dto.input.UpdateRoleCommand;
import com.bank.cards.application.dto.output.RoleResponse;
import com.bank.cards.domain.entity.Role;
import com.bank.cards.domain.exception.RoleAlreadyExistsException;
import com.bank.cards.domain.exception.RoleNotFoundException;
import com.bank.cards.domain.repository.RoleRepository;
import com.bank.cards.domain.valueobject.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateRoleUseCase {
    private final RoleRepository roleRepository;

    @Transactional
    public RoleResponse execute(UpdateRoleCommand command) {
        Role role = roleRepository.findById(command.id())
                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + command.id().value()));

        RoleName newName = new RoleName(command.newName());
        
        if (roleRepository.existsByName(newName)) {
            throw new RoleAlreadyExistsException(newName.value());
        }

        role.updateName(newName);
        Role updatedRole = roleRepository.save(role);
        
        return RoleResponse.from(updatedRole);
    }
}