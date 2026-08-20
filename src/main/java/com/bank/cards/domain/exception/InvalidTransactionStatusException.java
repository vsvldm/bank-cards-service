package com.bank.cards.domain.exception;

public class InvalidTransactionStatusException extends RuntimeException {
    public InvalidTransactionStatusException(String message) {
        super(message);
    }
}