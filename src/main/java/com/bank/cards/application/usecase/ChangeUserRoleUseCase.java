package com.bank.cards.application.usecase;

import com.bank.cards.application.dto.input.ChangeUserRoleCommand;
import com.bank.cards.application.dto.output.UserResponse;
import com.bank.cards.domain.entity.Role;
import com.bank.cards.domain.entity.User;
import com.bank.cards.domain.exception.RoleNotFoundException;
import com.bank.cards.domain.exception.UserNotFoundException;
import com.bank.cards.domain.exception.UserOperationException;
import com.bank.cards.domain.repository.RoleRepository;
import com.bank.cards.domain.repository.UserRepository;
import com.bank.cards.domain.valueobject.RoleName;
import com.bank.cards.domain.valueobject.Username;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangeUserRoleUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public UserResponse execute(ChangeUserRoleCommand command) {
        if (command.initiatorUsername().equals(command.targetUsername())) {
            throw new UserOperationException("User cannot modify their own roles");
        }

        User targetUser = userRepository.findByUsername(new Username(command.targetUsername()))
                .orElseThrow(() -> new UserNotFoundException("Target user not found: " + command.targetUsername()));

        RoleName roleName = new RoleName(command.roleName());
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + roleName.value()));

        if (command.isAdd()) {
            targetUser.addRole(role);
        } else {
            targetUser.removeRole(role);
        }

        User updatedUser = userRepository.save(targetUser);
        return UserResponse.from(updatedUser);
    }
}