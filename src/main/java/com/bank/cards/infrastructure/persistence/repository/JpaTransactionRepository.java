package com.bank.cards.infrastructure.persistence.repository;

import com.bank.cards.domain.valueobject.TransactionStatus;
import com.bank.cards.infrastructure.persistence.entity.TransactionJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaTransactionRepository extends JpaRepository<TransactionJpaEntity, UUID> {

    Page<TransactionJpaEntity> findBySourceCardId(UUID sourceCardId, Pageable pageable);

    Page<TransactionJpaEntity> findByTargetCardId(UUID targetCardId, Pageable pageable);

    @Query("SELECT t FROM TransactionJpaEntity t WHERE t.sourceCardId = :cardId OR t.targetCardId = :cardId")
    Page<TransactionJpaEntity> findBySourceCardIdOrTargetCardId(@Param("cardId") UUID cardId, Pageable pageable);

    long countBySourceCardId(UUID sourceCardId);

    long countByTargetCardId(UUID targetCardId);

    @Query("SELECT COUNT(t) FROM TransactionJpaEntity t WHERE t.sourceCardId = :cardId OR t.targetCardId = :cardId")
    long countBySourceCardIdOrTargetCardId(@Param("cardId") UUID cardId);

    Page<TransactionJpaEntity> findByStatus(TransactionStatus status, Pageable pageable);

    Page<TransactionJpaEntity> findBySourceCardIdAndStatus(UUID sourceCardId, TransactionStatus status, Pageable pageable);
}