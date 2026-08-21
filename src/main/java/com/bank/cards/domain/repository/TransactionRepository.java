package com.bank.cards.domain.repository;

import com.bank.cards.domain.entity.Transaction;
import com.bank.cards.domain.valueobject.CardId;
import com.bank.cards.domain.valueobject.TransactionId;
import com.bank.cards.domain.valueobject.TransactionStatus;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    Transaction save(Transaction transaction);

    Optional<Transaction> findById(TransactionId id);

    boolean existsById(TransactionId id);

    void deleteById(TransactionId id);

    List<Transaction> findBySourceCardId(CardId sourceCardId, int page, int size);

    List<Transaction> findByTargetCardId(CardId targetCardId, int page, int size);

    List<Transaction> findBySourceCardIdOrTargetCardId(CardId cardId, int page, int size);

    long countBySourceCardId(CardId sourceCardId);

    long countByTargetCardId(CardId targetCardId);

    long countBySourceCardIdOrTargetCardId(CardId cardId);

    List<Transaction> findByStatus(TransactionStatus status, int page, int size);

    List<Transaction> findBySourceCardIdAndStatus(CardId sourceCardId, TransactionStatus status, int page, int size);

    List<Transaction> findAll(int page, int size);

    long countAll();
}