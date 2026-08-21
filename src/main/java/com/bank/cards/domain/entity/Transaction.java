package com.bank.cards.domain.entity;

import com.bank.cards.domain.exception.InvalidTransactionStatusException;
import com.bank.cards.domain.valueobject.CardId;
import com.bank.cards.domain.valueobject.TransactionId;
import com.bank.cards.domain.valueobject.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private final TransactionId id;
    private final CardId sourceCardId;
    private final CardId targetCardId;
    private final BigDecimal amount;
    private TransactionStatus status;
    private final LocalDateTime createdAt;

    private Transaction(TransactionId id, CardId sourceCardId, CardId targetCardId,
                        BigDecimal amount, TransactionStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.sourceCardId = sourceCardId;
        this.targetCardId = targetCardId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
        validateInvariants();
    }

    public static Transaction create(CardId sourceCardId, CardId targetCardId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero");
        }
        if (sourceCardId.equals(targetCardId)) {
            throw new IllegalArgumentException("Source card and target card cannot be the same");
        }

        return new Transaction(
                TransactionId.generate(),
                sourceCardId,
                targetCardId,
                amount,
                TransactionStatus.PENDING,
                LocalDateTime.now()
        );
    }

    public void markAsCompleted() {
        if (this.status != TransactionStatus.PENDING) {
            throw new InvalidTransactionStatusException(
                    String.format("Cannot complete transaction. Current status: %s, expected: PENDING", this.status)
            );
        }
        this.status = TransactionStatus.COMPLETED;
    }

    public void markAsFailed() {
        if (this.status != TransactionStatus.PENDING) {
            throw new InvalidTransactionStatusException(
                    String.format("Cannot mark transaction as failed. Current status: %s, expected: PENDING", this.status)
            );
        }
        this.status = TransactionStatus.FAILED;
    }

    public void markAsCancelled() {
        if (this.status != TransactionStatus.PENDING) {
            throw new InvalidTransactionStatusException(
                    String.format("Cannot cancel transaction. Current status: %s, expected: PENDING", this.status)
            );
        }
        this.status = TransactionStatus.CANCELLED;
    }

    public TransactionId getId() { return id; }
    public CardId getSourceCardId() { return sourceCardId; }
    public CardId getTargetCardId() { return targetCardId; }
    public BigDecimal getAmount() { return amount; }
    public TransactionStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    private void validateInvariants() {
        if (id == null || sourceCardId == null || targetCardId == null || amount == null || status == null || createdAt == null) {
            throw new IllegalArgumentException("All transaction fields must be populated");
        }
    }
}