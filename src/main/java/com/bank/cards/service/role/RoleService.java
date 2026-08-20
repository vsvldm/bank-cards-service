package com.bank.cards.service.role;

import com.bank.cards.dto.role.RoleRequest;
import com.bank.cards.dto.role.RoleResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    RoleResponse create(RoleRequest roleRequest);

    List<RoleResponse> getAll(Pageable pageable);

    RoleResponse getByName(String roleName);

    void delete(Long roleId);

    RoleResponse update(Long roleId, RoleRequest updateRequest);
}
