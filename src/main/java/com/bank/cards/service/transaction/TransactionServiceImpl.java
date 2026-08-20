package com.bank.cards.service.transaction;

import com.bank.cards.domain.valueobject.CardStatus;
import com.bank.cards.dto.transaction.TransactionFullResponse;
import com.bank.cards.dto.transaction.TransactionRequest;
import com.bank.cards.dto.transaction.TransactionResponse;
import com.bank.cards.dto.transaction.TransactionUpdateRequest;
import com.bank.cards.entity.transaction.Transaction;
import com.bank.cards.entity.transaction.TransactionStatus;
import com.bank.cards.exception.exception.BadRequestException;
import com.bank.cards.exception.exception.CreationException;
import com.bank.cards.exception.exception.NotFoundException;
import com.bank.cards.infrastructure.persistence.entity.CardJpaEntity;
import com.bank.cards.infrastructure.persistence.repository.JpaCardRepository;
import com.bank.cards.mapper.TransactionMapper;
import com.bank.cards.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final JpaCardRepository cardRepository; // <-- ИЗМЕНЕНО
    private final TransactionMapper transactionMapper;

    @Override
    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {
        log.info("Creating transaction from {} to {}", request.getSourceCardId(), request.getTargetCardId());

        CardJpaEntity sourceCard = getCardById(request.getSourceCardId());
        CardJpaEntity targetCard = getCardById(request.getTargetCardId());

        Transaction transaction = transactionMapper.toTransaction(request, sourceCard, targetCard);
        String message;

        if (CardStatus.BLOCKED.name().equals(sourceCard.getStatus()) || CardStatus.BLOCKED.name().equals(targetCard.getStatus())) {
            transaction.setStatus(TransactionStatus.FAILED);
            message = "Cannot use blocked card";
        } else if (CardStatus.EXPIRED.name().equals(sourceCard.getStatus()) || CardStatus.EXPIRED.name().equals(targetCard.getStatus())) {
            transaction.setStatus(TransactionStatus.FAILED);
            message = "Cannot use expired card";
        } else if (sourceCard.getBalance().compareTo(request.getAmount()) < 0) {
            transaction.setStatus(TransactionStatus.FAILED);
            message = "Insufficient funds";
        } else {
            message = "Transaction in processing";
        }

        try {
            Transaction savedTransaction = transactionRepository.save(transaction);
            return transactionMapper.toTransactionResponse(savedTransaction, message);
        } catch (Exception e) {
            log.error("Failed to create transaction: {}", e.getMessage());
            throw new CreationException(String.format("Failed to create transaction: %s", e.getMessage()));
        }
    }

    @Override
    @Transactional
    public TransactionResponse updateStatusTransaction(Long transactionId, TransactionUpdateRequest request) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException(String.format("Transaction with id=%d not found", transactionId)));

        if (transaction.getStatus() == request.getStatus()) {
            throw new BadRequestException(String.format("Transaction status is already %s", transaction.getStatus()));
        }

        transaction.setStatus(request.getStatus());
        Transaction updatedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toTransactionResponse(updatedTransaction, "Status changed");
    }

    @Override
    public List<TransactionFullResponse> getTransactions(UUID sourceCardId, TransactionStatus status, Pageable pageable) {
        Page<Transaction> page = transactionRepository.findAll(pageable);
        return page.stream()
                .filter(t -> sourceCardId == null || (t.getSourceCard() != null && t.getSourceCard().getId().equals(sourceCardId)))
                .filter(t -> status == null || t.getStatus() == status)
                .map(transactionMapper::toFullResponse)
                .toList();
    }

    @Override
    public List<TransactionFullResponse> getTransactionsByCard(UUID cardId, TransactionStatus status, Pageable pageable) {
        return getTransactions(cardId, status, pageable);
    }

    private CardJpaEntity getCardById(UUID cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException(String.format("Card with id=%s not found", cardId)));
    }
}