package com.bank.cards.controller.user;

import com.bank.cards.dto.user.UserRegistrationRequest;
import com.bank.cards.dto.user.UserResponse;
import com.bank.cards.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
@Tag(name = "UserPublicController", description = "Регистрация пользователей")
public class UserPublicController {
    private final UserService userService;

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
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest) {
        return userService.create(userRegistrationRequest);
    }
}
