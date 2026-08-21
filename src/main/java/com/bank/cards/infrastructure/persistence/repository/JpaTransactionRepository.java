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

/**
 * Spring Data JPA репозиторий для работы с транзакциями в базе данных.
 */
@Repository
public interface JpaTransactionRepository extends JpaRepository<TransactionJpaEntity, UUID> {

    /**
     * Поиск транзакций по ID карты-отправителя с пагинацией.
     */
    Page<TransactionJpaEntity> findBySourceCardId(UUID sourceCardId, Pageable pageable);

    /**
     * Поиск транзакций по ID карты-получателя с пагинацией.
     */
    Page<TransactionJpaEntity> findByTargetCardId(UUID targetCardId, Pageable pageable);

    /**
     * Поиск всех транзакций, где карта участвует как отправитель или получатель.
     */
    @Query("SELECT t FROM TransactionJpaEntity t WHERE t.sourceCardId = :cardId OR t.targetCardId = :cardId")
    Page<TransactionJpaEntity> findBySourceCardIdOrTargetCardId(@Param("cardId") UUID cardId, Pageable pageable);

    /**
     * Подсчет транзакций по ID карты-отправителя.
     */
    long countBySourceCardId(UUID sourceCardId);

    /**
     * Подсчет транзакций по ID карты-получателя.
     */
    long countByTargetCardId(UUID targetCardId);

    /**
     * Подсчет всех транзакций, где карта участвует как отправитель или получатель.
     */
    @Query("SELECT COUNT(t) FROM TransactionJpaEntity t WHERE t.sourceCardId = :cardId OR t.targetCardId = :cardId")
    long countBySourceCardIdOrTargetCardId(@Param("cardId") UUID cardId);

    /**
     * Поиск транзакций по статусу с пагинацией.
     */
    Page<TransactionJpaEntity> findByStatus(TransactionStatus status, Pageable pageable);

    /**
     * Поиск транзакций по ID карты-отправителя и статусу с пагинацией.
     */
    Page<TransactionJpaEntity> findBySourceCardIdAndStatus(UUID sourceCardId, TransactionStatus status, Pageable pageable);
}