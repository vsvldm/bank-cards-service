package com.bank.cards.presentation.controller;

import com.bank.cards.application.dto.input.UpdateUserProfileCommand;
import com.bank.cards.application.dto.output.UserResponse;
import com.bank.cards.application.usecase.UpdateUserProfileUseCase;
import com.bank.cards.presentation.dto.request.UserUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "UserPrivateController", description = "Профиль пользователя")
@SecurityRequirement(name = "JWT")
public class PrivateUserController {

    private final UpdateUserProfileUseCase updateUserProfileUseCase;

    @Operation(
            summary = "Обновить профиль",
            description = "Изменение данных пользователя. Требует роли USER"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль обновлен"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "409", description = "Логин/email уже занят")
    })
    @PatchMapping
    public ResponseEntity<UserResponse> update(
            @Parameter(hidden = true) Principal principal,
            @RequestBody @Valid UserUpdateRequest request) {

        UpdateUserProfileCommand command = new UpdateUserProfileCommand(
                principal.getName(),
                request.username(),
                request.password(),
                request.email()
        );

        UserResponse response = updateUserProfileUseCase.execute(command);
        return ResponseEntity.ok(response);
    }
}