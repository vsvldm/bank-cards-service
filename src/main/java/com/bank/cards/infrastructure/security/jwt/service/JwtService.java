package com.bank.cards.infrastructure.security.jwt.service;

import com.bank.cards.presentation.dto.request.JwtRequest;
import com.bank.cards.application.dto.output.JwtResponse;

public interface JwtService {
    JwtResponse createToken(JwtRequest jwtRequest);
}
