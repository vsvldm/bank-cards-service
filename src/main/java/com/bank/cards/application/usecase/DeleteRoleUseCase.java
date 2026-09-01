package com.bank.cards.application.usecase;

import com.bank.cards.domain.entity.Role;
import com.bank.cards.domain.exception.RoleNotFoundException;
import com.bank.cards.domain.exception.SystemRoleModificationException;
import com.bank.cards.domain.repository.RoleRepository;
import com.bank.cards.domain.valueobject.RoleId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteRoleUseCase {
    private final RoleRepository roleRepository;

    @Transactional
    public void execute(RoleId id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + id.value()));

        if (role.isDefaultRole()) {
            throw new SystemRoleModificationException("The default role cannot be removed");
        }

        roleRepository.deleteById(id);
    }
}