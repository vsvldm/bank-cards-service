package com.bank.cards.presentation.controller;

import com.bank.cards.application.dto.input.ChangeUserRoleCommand;
import com.bank.cards.application.dto.output.UserResponse;
import com.bank.cards.application.usecase.ChangeUserRoleUseCase;
import com.bank.cards.application.usecase.DeleteUserUseCase;
import com.bank.cards.application.usecase.GetUsersUseCase;
import com.bank.cards.domain.valueobject.ChangeRoleType;
import com.bank.cards.presentation.dto.request.ChangeRoleRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Tag(name = "UserAdminController", description = "Управление пользователями (админ)")
@SecurityRequirement(name = "JWT")
public class AdminUserController {

    private final ChangeUserRoleUseCase changeUserRoleUseCase;
    private final GetUsersUseCase getUsersUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    @Operation(
            summary = "Изменить роль пользователя",
            description = "Добавление или удаление роли у пользователя. Требует роли ADMIN"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Роль изменена"),
            @ApiResponse(responseCode = "400", description = "Невозможно изменить системные роли"),
            @ApiResponse(responseCode = "404", description = "Пользователь или роль не найдены")
    })
    @PatchMapping("/role")
    public ResponseEntity<UserResponse> changeRole(
            @Parameter(hidden = true) Principal principal,
            @RequestBody @Valid ChangeRoleRequest request) {

        boolean isAdd = request.operationType() == ChangeRoleType.ADD;

        ChangeUserRoleCommand command = new ChangeUserRoleCommand(
                principal.getName(),
                request.username(),
                request.role(),
                isAdd
        );

        UserResponse response = changeUserRoleUseCase.execute(command);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Получить всех пользователей",
            description = "Список пользователей с пагинацией. Требует роли ADMIN",
            parameters = {
                    @Parameter(name = "page", description = "Номер страницы", example = "0", in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "Размер страницы", example = "10", in = ParameterIn.QUERY),
                    @Parameter(name = "sort", description = "Поле сортировки", example = "username", in = ParameterIn.QUERY)
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username") String sort) {

        return ResponseEntity.ok(getUsersUseCase.getAll(page, size, sort));
    }

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Получение детальной информации о пользователе. Требует роли ADMIN",
            parameters = @Parameter(name = "userId", description = "ID пользователя (UUID)", example = "00000000-0000-0000-0000-000000000001", in = ParameterIn.PATH)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getById(@PathVariable String userId) {
        return ResponseEntity.ok(getUsersUseCase.getById(userId));
    }

    @Operation(
            summary = "Удалить пользователя",
            description = "Удаление пользователя по ID. Требует роли ADMIN",
            parameters = @Parameter(name = "userId", description = "ID пользователя (UUID)", example = "00000000-0000-0000-0000-000000000001", in = ParameterIn.PATH)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь удален"),
            @ApiResponse(responseCode = "400", description = "Нельзя удалить текущего пользователя"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(
            @PathVariable String userId,
            @Parameter(hidden = true) Principal principal) {

        deleteUserUseCase.execute(userId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}