package com.bank.cards.application.usecase;

import com.bank.cards.application.dto.input.UpdateTransactionStatusCommand;
import com.bank.cards.application.dto.output.TransactionResponse;
import com.bank.cards.domain.entity.Transaction;
import com.bank.cards.domain.exception.TransactionNotFoundException;
import com.bank.cards.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateTransactionStatusUseCase {

    private final TransactionRepository transactionRepository;

    public UpdateTransactionStatusUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionResponse execute(UpdateTransactionStatusCommand command) {
        Transaction transaction = transactionRepository.findById(command.transactionId())
                .orElseThrow(() -> new TransactionNotFoundException(
                        "Transaction not found: " + command.transactionId().value()
                ));

        switch (command.newStatus()) {
            case COMPLETED -> transaction.markAsCompleted();
            case FAILED -> transaction.markAsFailed();
            case CANCELLED -> transaction.markAsCancelled();
            default -> throw new IllegalArgumentException("Invalid new status: " + command.newStatus());
        }

        Transaction updated = transactionRepository.save(transaction);
        return TransactionResponse.from(updated);
    }
}