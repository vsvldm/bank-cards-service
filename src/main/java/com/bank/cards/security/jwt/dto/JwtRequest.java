package com.bank.cards.security.jwt.dto;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
