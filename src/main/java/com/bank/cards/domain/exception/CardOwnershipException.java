package com.bank.cards.domain.exception;

public class CardOwnershipException extends RuntimeException {
    public CardOwnershipException(String message) {
        super(message);
    }
}
