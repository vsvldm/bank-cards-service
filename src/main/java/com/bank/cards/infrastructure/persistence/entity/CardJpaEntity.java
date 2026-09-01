package com.bank.cards.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "bank_cards", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_status", columnList = "status")
})
@Getter
@NoArgsConstructor
public class CardJpaEntity {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "encrypted_card_number", nullable = false, length = 500)
    private String encryptedCardNumber;

    @Column(name = "encrypted_cvv", nullable = false, length = 500)
    private String encryptedCvv;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Builder
    private CardJpaEntity(UUID id, UUID userId, String encryptedCardNumber,
                          String encryptedCvv, LocalDate expiryDate,
                          BigDecimal balance, String status) {
        this.id = id;
        this.userId = userId;
        this.encryptedCardNumber = encryptedCardNumber;
        this.encryptedCvv = encryptedCvv;
        this.expiryDate = expiryDate;
        this.balance = balance;
        this.status = status;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    public void updateStatus(String newStatus) {
        if (newStatus == null || newStatus.isBlank()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        this.status = newStatus;
        this.updatedAt = LocalDate.now();
    }

    public void updateBalance(BigDecimal newBalance) {
        if (newBalance == null || newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be null or negative");
        }
        this.balance = newBalance;
        this.updatedAt = LocalDate.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardJpaEntity that = (CardJpaEntity) o;
        if (id == null || that.id == null) return super.equals(o);
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "CardJpaEntity{id=" + id + ", userId=" + userId +
                ", status='" + status + "', balance=" + balance + "}";
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }
}