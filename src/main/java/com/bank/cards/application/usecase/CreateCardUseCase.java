package com.bank.cards.application.usecase;

import com.bank.cards.application.dto.input.CreateCardCommand;
import com.bank.cards.application.dto.output.CardResponse;
import com.bank.cards.application.port.UserQueryPort;
import com.bank.cards.domain.entity.BankCard;
import com.bank.cards.domain.exception.TooManyCardsException;
import com.bank.cards.domain.port.EncryptionPort;
import com.bank.cards.domain.repository.CardRepository;
import com.bank.cards.domain.valueobject.EncryptedData;
import com.bank.cards.domain.valueobject.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateCardUseCase {

    private static final int MAX_CARDS_PER_USER = 5;

    private final CardRepository cardRepository;
    private final EncryptionPort encryptionPort;
    private final UserQueryPort userQueryPort;

    @Transactional
    public CardResponse execute(CreateCardCommand command) {
        UserId userId = userQueryPort.getUserIdByUsername(command.username());

        long existingCards = cardRepository.countByUserId(userId);
        if (existingCards >= MAX_CARDS_PER_USER) {
            throw new TooManyCardsException(userId, existingCards, MAX_CARDS_PER_USER);
        }

        EncryptedData encryptedNumber = encryptionPort.encrypt(command.cardNumber().value());
        EncryptedData encryptedCvv = encryptionPort.encrypt(command.cvv());

        BankCard newCard = BankCard.createNew(
                userId, encryptedNumber, encryptedCvv, command.expiryDate());

        BankCard savedCard = cardRepository.save(newCard);

        return CardResponse.from(savedCard, encryptionPort);
    }
}