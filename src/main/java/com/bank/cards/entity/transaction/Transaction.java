package com.bank.cards.entity.transaction;

import com.bank.cards.infrastructure.persistence.entity.CardJpaEntity; // <-- НОВЫЙ ИМПОРТ
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_card_id", nullable = false)
    private CardJpaEntity sourceCard;

    @ManyToOne
    @JoinColumn(name = "target_card_id", nullable = false)
    private CardJpaEntity targetCard;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(name = "timestamp", updatable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    @PrePersist
    private void setDefaultStatus() {
        if (status == null) {
            status = TransactionStatus.PENDING;
        }
    }
}