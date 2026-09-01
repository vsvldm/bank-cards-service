package com.bank.cards.presentation.controller;

import com.bank.cards.application.dto.input.CreateRoleCommand;
import com.bank.cards.application.dto.input.UpdateRoleCommand;
import com.bank.cards.application.dto.output.RoleResponse;
import com.bank.cards.application.usecase.CreateRoleUseCase;
import com.bank.cards.application.usecase.DeleteRoleUseCase;
import com.bank.cards.application.usecase.GetRolesUseCase;
import com.bank.cards.application.usecase.UpdateRoleUseCase;
import com.bank.cards.domain.valueobject.RoleId;
import com.bank.cards.presentation.dto.request.RoleRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class RoleAdminController {
    
    private final CreateRoleUseCase createRoleUseCase;
    private final UpdateRoleUseCase updateRoleUseCase;
    private final DeleteRoleUseCase deleteRoleUseCase;
    private final GetRolesUseCase getRolesUseCase;

    @PostMapping
    public ResponseEntity<RoleResponse> create(@Valid @RequestBody RoleRequest roleRequest) {
        RoleResponse response = createRoleUseCase.execute(new CreateRoleCommand(roleRequest.name()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<RoleResponse> update(@PathVariable String roleId, @Valid @RequestBody RoleRequest request) {
        RoleResponse response = updateRoleUseCase.execute(new UpdateRoleCommand(RoleId.fromString(roleId), request.name()));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sort) {
        return ResponseEntity.ok(getRolesUseCase.getAll(page, size, sort));
    }

    @GetMapping("/{roleName}")
    public ResponseEntity<RoleResponse> getByName(@PathVariable String roleName) {
        return ResponseEntity.ok(getRolesUseCase.getByName(roleName));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> delete(@PathVariable String roleId) {
        deleteRoleUseCase.execute(RoleId.fromString(roleId));
        return ResponseEntity.noContent().build();
    }
}