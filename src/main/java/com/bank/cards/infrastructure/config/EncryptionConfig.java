package com.bank.cards.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptionConfig {

    @Value("${encryption.key}")
    private String encryptionKey;

    public void validateKey() {
        if (encryptionKey == null || encryptionKey.length() != 32) {
            throw new IllegalStateException(
                    "Encryption key must be 32 characters (256 bits) for AES-256"
            );
        }
    }
}