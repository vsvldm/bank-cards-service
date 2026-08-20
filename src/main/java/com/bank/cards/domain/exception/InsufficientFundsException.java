package com.bank.cards.domain.exception;

import com.bank.cards.domain.valueobject.CardId;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(CardId cardId, BigDecimal balance, BigDecimal requested) {
        super(String.format("Insufficient funds on card %s. Balance: %s, requested: %s",
                cardId.value(), balance, requested));
    }
}