package com.bank.cards.domain.exception;

import com.bank.cards.domain.valueobject.CardId;

public class CardNotFoundException extends RuntimeException {

    public CardNotFoundException(CardId cardId) {
        super("Card not found: " + cardId.value());
    }

    public CardNotFoundException(String message) {
        super(message);
    }
}
