package com.bank.cards.security.jwt.service;

import com.bank.cards.security.jwt.dto.JwtRequest;
import com.bank.cards.security.jwt.dto.JwtResponse;

public interface JwtService {
    JwtResponse createToken(JwtRequest jwtRequest);
}
