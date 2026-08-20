package com.bank.cards.domain.exception;

import com.bank.cards.domain.valueobject.CardId;

public class CardBlockedException extends RuntimeException {
    public CardBlockedException(CardId cardId) {
        super("Card is blocked: " + cardId.value());
    }}
