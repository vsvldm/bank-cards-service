package com.bank.cards.application.dto.output;

import com.bank.cards.domain.entity.Transaction;
import com.bank.cards.domain.valueobject.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionFullResponse(
        UUID id,
        UUID sourceCardId,
        UUID targetCardId,
        BigDecimal amount,
        TransactionStatus status,
        LocalDateTime createdAt,
        String description
) {
    public static TransactionFullResponse from(Transaction transaction, String description) {
        return new TransactionFullResponse(
                transaction.getId().value(),
                transaction.getSourceCardId().value(),
                transaction.getTargetCardId().value(),
                transaction.getAmount(),
                transaction.getStatus(),
                transaction.getCreatedAt(),
                description
        );
    }

    public static TransactionFullResponse from(Transaction transaction) {
        return from(transaction, null);
    }
}