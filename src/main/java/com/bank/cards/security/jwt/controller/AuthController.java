package com.bank.cards.security.jwt.controller;

import com.bank.cards.security.jwt.dto.JwtRequest;
import com.bank.cards.security.jwt.dto.JwtResponse;
import com.bank.cards.security.jwt.service.JwtService;
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
