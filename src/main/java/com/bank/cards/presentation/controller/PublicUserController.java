package com.bank.cards.presentation.controller;

import com.bank.cards.application.dto.input.RegisterUserCommand;
import com.bank.cards.application.dto.output.UserResponse;
import com.bank.cards.application.usecase.RegisterUserUseCase;
import com.bank.cards.presentation.dto.request.UserRegistrationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
@Tag(name = "PublicUserController", description = "Регистрация пользователей")
public class PublicUserController {

    private final RegisterUserUseCase registerUserUseCase;

    @Operation(
            summary = "Зарегистрировать пользователя",
            description = "Создание нового аккаунта (публичный доступ)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные регистрации"),
            @ApiResponse(responseCode = "409", description = "Логин/email уже занят")
    })
    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRegistrationRequest request) {
        RegisterUserCommand command = new RegisterUserCommand(
                request.username(),
                request.password(),
                request.email()
        );

        UserResponse response = registerUserUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}