package com.bank.cards.application.usecase;

import com.bank.cards.application.dto.output.RoleResponse;
import com.bank.cards.domain.entity.Role;
import com.bank.cards.domain.exception.RoleNotFoundException;
import com.bank.cards.domain.repository.RoleRepository;
import com.bank.cards.domain.valueobject.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetRolesUseCase {
    private final RoleRepository roleRepository;

    public List<RoleResponse> getAll(int page, int size, String sort) {
        return roleRepository.findAll(page, size, sort).stream()
                .map(RoleResponse::from)
                .toList();
    }

    public RoleResponse getByName(String roleName) {
        RoleName nameVO = new RoleName(roleName);
        Role role = roleRepository.findByName(nameVO)
                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + nameVO.value()));
        return RoleResponse.from(role);
    }
}