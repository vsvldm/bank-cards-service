package com.bank.cards.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.cards")
public record CardProperties(

        int maxPerUser
) {
    public CardProperties {
        if (maxPerUser <= 0) {
            maxPerUser = 5;
        }
    }
}