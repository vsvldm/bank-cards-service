package com.bank.cards.application.usecase;

import com.bank.cards.application.dto.input.TransferCommand;
import com.bank.cards.domain.entity.BankCard;
import com.bank.cards.domain.entity.Transaction;
import com.bank.cards.domain.exception.CardNotFoundException;
import com.bank.cards.domain.exception.CardOwnershipException;
import com.bank.cards.domain.repository.CardRepository;
import com.bank.cards.domain.repository.TransactionRepository;
import com.bank.cards.domain.valueobject.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferMoneyUseCase {

    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void execute(TransferCommand command, UserId ownerId) {
        BankCard fromCard = cardRepository.findById(command.fromCardId())
                .orElseThrow(() -> new CardNotFoundException(command.fromCardId()));

        BankCard toCard = cardRepository.findById(command.toCardId())
                .orElseThrow(() -> new CardNotFoundException(command.toCardId()));

        if (!fromCard.getUserId().equals(ownerId)) {
            throw new CardOwnershipException("Source card does not belong to user");
        }
        if (!toCard.getUserId().equals(ownerId)) {
            throw new CardOwnershipException("Target card does not belong to user");
        }

        fromCard.withdraw(command.amount());
        toCard.deposit(command.amount());

        cardRepository.save(fromCard);
        cardRepository.save(toCard);

        Transaction transaction = Transaction.create(command.fromCardId(), command.toCardId(), command.amount());
        transaction.markAsCompleted();

        transactionRepository.save(transaction);
    }
}