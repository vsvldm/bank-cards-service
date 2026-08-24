package com.bank.cards.infrastructure.persistence.entity;

import com.bank.cards.domain.valueobject.TransactionStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions", indexes = {
        @Index(name = "idx_source_card_id", columnList = "source_card_id"),
        @Index(name = "idx_target_card_id", columnList = "target_card_id"),
        @Index(name = "idx_status", columnList = "status")
})
@Getter
@NoArgsConstructor
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
    @Column(name = "status", nullable = false, length = 20)
    private TransactionStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    private TransactionJpaEntity(UUID id, UUID sourceCardId, UUID targetCardId,
                                 BigDecimal amount, TransactionStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.sourceCardId = sourceCardId;
        this.targetCardId = targetCardId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public void updateStatus(TransactionStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Transaction status cannot be null");
        }
        this.status = newStatus;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionJpaEntity that = (TransactionJpaEntity) o;
        if (id == null || that.id == null) return super.equals(o);
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}