package com.bank.cards.domain.port;

import com.bank.cards.domain.valueobject.EncodedPassword;

public interface PasswordEncoderPort {
    EncodedPassword encode(String rawPassword);
}