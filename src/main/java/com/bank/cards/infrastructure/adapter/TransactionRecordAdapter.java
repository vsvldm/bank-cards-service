package com.bank.cards.infrastructure.adapter;

import com.bank.cards.application.port.TransactionRecordPort;
import com.bank.cards.domain.valueobject.CardId;
import com.bank.cards.entity.transaction.Transaction;
import com.bank.cards.entity.transaction.TransactionStatus;
import com.bank.cards.infrastructure.persistence.entity.CardJpaEntity;
import com.bank.cards.infrastructure.persistence.repository.JpaCardRepository;
import com.bank.cards.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TransactionRecordAdapter implements TransactionRecordPort {

    private final TransactionRepository transactionRepository;
    private final JpaCardRepository jpaCardRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordTransfer(CardId fromCardId, CardId toCardId, BigDecimal amount) {
        CardJpaEntity source = jpaCardRepository.findById(fromCardId.value())
                .orElseThrow(() -> new IllegalArgumentException("Source card not found"));
        CardJpaEntity target = jpaCardRepository.findById(toCardId.value())
                .orElseThrow(() -> new IllegalArgumentException("Target card not found"));

        Transaction transaction = Transaction.builder()
                .sourceCard(source)
                .targetCard(target)
                .amount(amount)
                .status(TransactionStatus.SUCCESS)
                .build();

        transactionRepository.save(transaction);
    }
}