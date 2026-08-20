package com.bank.cards.application.usecase;

import com.bank.cards.application.dto.input.TransferCommand;
import com.bank.cards.application.port.TransactionRecordPort;
import com.bank.cards.domain.entity.BankCard;
import com.bank.cards.domain.exception.CardNotFoundException;
import com.bank.cards.domain.repository.CardRepository;
import com.bank.cards.domain.valueobject.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferMoneyUseCase {

    private final CardRepository cardRepository;
    private final TransactionRecordPort transactionRecordPort;

    @Transactional
    public void execute(TransferCommand command, UserId ownerId) {
        BankCard fromCard = cardRepository.findById(command.fromCardId())
                .orElseThrow(() -> new CardNotFoundException(command.fromCardId()));
        BankCard toCard = cardRepository.findById(command.toCardId())
                .orElseThrow(() -> new CardNotFoundException(command.toCardId()));

        if (!fromCard.getUserId().equals(ownerId)) {
            throw new IllegalArgumentException("Source card does not belong to user");
        }
        if (!toCard.getUserId().equals(ownerId)) {
            throw new IllegalArgumentException("Target card does not belong to user");
        }

        fromCard.withdraw(command.amount());
        toCard.deposit(command.amount());

        cardRepository.save(fromCard);
        cardRepository.save(toCard);

        transactionRecordPort.recordTransfer(command.fromCardId(), command.toCardId(), command.amount());
    }
}