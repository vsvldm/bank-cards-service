package com.bank.cards.domain.exception;

import com.bank.cards.domain.valueobject.UserId;

public class TooManyCardsException extends RuntimeException {
    public TooManyCardsException(UserId userId, long currentCount, long maxAllowed) {
        super(String.format("User %d already has %d cards. Maximum allowed: %d",
                userId.value(), currentCount, maxAllowed
        ));
    }}
