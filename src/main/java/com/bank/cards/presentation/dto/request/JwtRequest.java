package com.bank.cards.presentation.dto.request;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
