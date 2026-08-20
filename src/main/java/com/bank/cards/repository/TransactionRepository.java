package com.bank.cards.repository;

import com.bank.cards.entity.transaction.Transaction;
import com.bank.cards.entity.transaction.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Collection<Transaction> findByStatus(TransactionStatus status);
}
