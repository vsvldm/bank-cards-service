package com.bank.cards.mapper;

import com.bank.cards.dto.role.RoleResponse;
import com.bank.cards.entity.role.Role;
import org.springframework.stereotype.Component;


@Component
public class RoleMapper {
    public RoleResponse toRoleResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}
