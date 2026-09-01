package com.bank.cards.infrastructure.security.adapter;

import com.bank.cards.domain.port.PasswordEncoderPort;
import com.bank.cards.domain.valueobject.EncodedPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder springPasswordEncoder;

    @Override
    public EncodedPassword encode(String rawPassword) {
        String encoded = springPasswordEncoder.encode(rawPassword);
        return new EncodedPassword(encoded);
    }
}