package com.bank.cards.application.usecase;

import com.bank.cards.application.dto.input.CreateTransactionCommand;
import com.bank.cards.application.dto.output.TransactionResponse;
import com.bank.cards.domain.entity.Transaction;
import com.bank.cards.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateTransactionUseCase {

    private final TransactionRepository transactionRepository;

    public CreateTransactionUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionResponse execute(CreateTransactionCommand command) {
        Transaction transaction = Transaction.create(
                command.sourceCardId(),
                command.targetCardId(),
                command.amount()
        );

        Transaction saved = transactionRepository.save(transaction);
        return TransactionResponse.from(saved);
    }
}