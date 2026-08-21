package com.bank.cards.infrastructure.persistence.adapter;

import com.bank.cards.domain.entity.Transaction;
import com.bank.cards.domain.repository.TransactionRepository;
import com.bank.cards.domain.valueobject.CardId;
import com.bank.cards.domain.valueobject.TransactionId;
import com.bank.cards.domain.valueobject.TransactionStatus;
import com.bank.cards.infrastructure.persistence.entity.TransactionJpaEntity;
import com.bank.cards.infrastructure.persistence.repository.JpaTransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Адаптер для доменного репозитория TransactionRepository.
 * Реализует интерфейс доменного слоя и использует JpaTransactionRepository для работы с БД.
 */
@Component
public class TransactionRepositoryAdapter implements TransactionRepository {

    private final JpaTransactionRepository jpaRepository;

    public TransactionRepositoryAdapter(JpaTransactionRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionJpaEntity entity = toJpaEntity(transaction);
        TransactionJpaEntity saved = jpaRepository.save(entity);
        return toDomainEntity(saved);
    }

    @Override
    public Optional<Transaction> findById(TransactionId id) {
        return jpaRepository.findById(id.value())
                .map(this::toDomainEntity);
    }

    @Override
    public boolean existsById(TransactionId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public void deleteById(TransactionId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public List<Transaction> findBySourceCardId(CardId sourceCardId, int page, int size) {
        Page<TransactionJpaEntity> result = jpaRepository.findBySourceCardId(
                sourceCardId.value(),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        return result.getContent().stream()
                .map(this::toDomainEntity)
                .toList();
    }

    @Override
    public List<Transaction> findByTargetCardId(CardId targetCardId, int page, int size) {
        Page<TransactionJpaEntity> result = jpaRepository.findByTargetCardId(
                targetCardId.value(),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        return result.getContent().stream()
                .map(this::toDomainEntity)
                .toList();
    }

    @Override
    public List<Transaction> findBySourceCardIdOrTargetCardId(CardId cardId, int page, int size) {
        Page<TransactionJpaEntity> result = jpaRepository.findBySourceCardIdOrTargetCardId(
                cardId.value(),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        return result.getContent().stream()
                .map(this::toDomainEntity)
                .toList();
    }

    @Override
    public long countBySourceCardId(CardId sourceCardId) {
        return jpaRepository.countBySourceCardId(sourceCardId.value());
    }

    @Override
    public long countByTargetCardId(CardId targetCardId) {
        return jpaRepository.countByTargetCardId(targetCardId.value());
    }

    @Override
    public long countBySourceCardIdOrTargetCardId(CardId cardId) {
        return jpaRepository.countBySourceCardIdOrTargetCardId(cardId.value());
    }

    @Override
    public List<Transaction> findByStatus(TransactionStatus status, int page, int size) {
        Page<TransactionJpaEntity> result = jpaRepository.findByStatus(
                status,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        return result.getContent().stream()
                .map(this::toDomainEntity)
                .toList();
    }

    @Override
    public List<Transaction> findBySourceCardIdAndStatus(CardId sourceCardId, TransactionStatus status, int page, int size) {
        Page<TransactionJpaEntity> result = jpaRepository.findBySourceCardIdAndStatus(
                sourceCardId.value(),
                status,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        return result.getContent().stream()
                .map(this::toDomainEntity)
                .toList();
    }

    @Override
    public List<Transaction> findAll(int page, int size) {
        Page<TransactionJpaEntity> result = jpaRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        return result.getContent().stream()
                .map(this::toDomainEntity)
                .toList();
    }

    @Override
    public long countAll() {
        return jpaRepository.count();
    }

    // --- Методы маппинга между доменной сущностью и JPA-сущностью ---

    /**
     * Преобразование доменной сущности Transaction в JPA-сущность TransactionJpaEntity.
     */
    private TransactionJpaEntity toJpaEntity(Transaction transaction) {
        return new TransactionJpaEntity(
                transaction.getId().value(),
                transaction.getSourceCardId().value(),
                transaction.getTargetCardId().value(),
                transaction.getAmount(),
                transaction.getStatus(),
                transaction.getCreatedAt()
        );
    }

    /**
     * Преобразование JPA-сущности TransactionJpaEntity в доменную сущность Transaction.
     * Использует приватный конструктор через reflection или фабричный метод.
     */
    private Transaction toDomainEntity(TransactionJpaEntity entity) {
        // Создаем доменную сущность через фабричный метод или reflection
        // Здесь используем приватный конструктор через reflection
        try {
            var constructor = Transaction.class.getDeclaredConstructor(
                    TransactionId.class,
                    CardId.class,
                    CardId.class,
                    java.math.BigDecimal.class,
                    TransactionStatus.class,
                    java.time.LocalDateTime.class
            );
            constructor.setAccessible(true);
            return constructor.newInstance(
                    new TransactionId(entity.getId()),
                    new CardId(entity.getSourceCardId()),
                    new CardId(entity.getTargetCardId()),
                    entity.getAmount(),
                    entity.getStatus(),
                    entity.getCreatedAt()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to create domain Transaction entity from JPA entity", e);
        }
    }
}