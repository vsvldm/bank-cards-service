package com.bank.cards.infrastructure.persistence.entity;

import com.bank.cards.domain.valueobject.TransactionStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA-сущность для хранения транзакций в базе данных.
 * Маппится на таблицу "transactions".
 */
@Entity
@Table(name = "transactions")
public class TransactionJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "source_card_id", nullable = false)
    private UUID sourceCardId;

    @Column(name = "target_card_id", nullable = false)
    private UUID targetCardId;

    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Конструктор по умолчанию для JPA
    protected TransactionJpaEntity() {}

    public TransactionJpaEntity(UUID id, UUID sourceCardId, UUID targetCardId,
                                BigDecimal amount, TransactionStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.sourceCardId = sourceCardId;
        this.targetCardId = targetCardId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    // --- Геттеры и сеттеры ---

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSourceCardId() {
        return sourceCardId;
    }

    public void setSourceCardId(UUID sourceCardId) {
        this.sourceCardId = sourceCardId;
    }

    public UUID getTargetCardId() {
        return targetCardId;
    }

    public void setTargetCardId(UUID targetCardId) {
        this.targetCardId = targetCardId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}