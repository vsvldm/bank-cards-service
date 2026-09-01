package com.bank.cards.presentation.controller;

import com.bank.cards.presentation.dto.request.JwtRequest;
import com.bank.cards.application.dto.output.JwtResponse;
import com.bank.cards.infrastructure.security.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtService jwtService;

    @PostMapping
    public JwtResponse createToken(@RequestBody JwtRequest jwtRequest) {
        return jwtService.createToken(jwtRequest);
    }
}
